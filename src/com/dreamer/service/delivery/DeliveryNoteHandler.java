package com.dreamer.service.delivery;

import com.dreamer.domain.goods.*;
import com.dreamer.domain.mall.shopcart.ShopCart;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.goods.*;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.service.goods.AgentLevelTradingLimitedHandler;
import com.dreamer.service.goods.TransferHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.BalanceNotEnoughException;
import ps.mx.otter.exception.DataNotFoundException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DeliveryNoteHandler {

	@Transactional
	public void addDeliveryNote(DeliveryNote note, String consigneeAgentCode,
			Integer[] quantitys, Integer[] goodsIds) {
		assembleAgent(consigneeAgentCode, note);
		Agent applicant = note.getUserByApplyAgent();
		List<Goods> goodses = assembleGoods(goodsIds);
		for (int index = 0; index < goodsIds.length; index++) {
			/*
			 * //已经向申请发货人发起的尚未处理的转货申请 Integer
			 * alreadyApplyTransferQuantity=transferDAO
			 * .sumNoConfirmApplyQuantity(applicant.getId(),
			 * goodses.get(index).getId()).intValue(); //申请发货人已经申请的发货未处理记录
			 * Integer
			 * alreadyApplyQuantity=deliveryItemDAO.sumNoConfirmNewApplyQuantity
			 * (applicant.getId(), goodses.get(index).getId()).intValue();
			 * //申请发货人可用余额=当前余额-(申请人已经申请的发货未处理数+下级向申请人发起的转货未处理数)
			 * if(applicant.isBalanceNotEnough
			 * (goodses.get(index),quantitys[index
			 * ]+alreadyApplyQuantity+alreadyApplyTransferQuantity)){ throw new
			 * ApplicationException
			 * (goodses.get(index).getName()+" 账户余额不足,转货申请失败"); }
			 */
			agentLevelTradingLimitedHandler.checkBalance(applicant,
					goodses.get(index), quantitys[index]);
			agentLevelTradingLimitedHandler.checkTradingLimit(applicant,
					goodses.get(index), quantitys[index]);
		}
        Logistics logistics = logisticsDAO.findByProvinces(note.getProvince());
        note.apply(goodses, quantitys,logistics);//申请订单
        //不是商城申请发货,扣减申请人的物流费
        if(!note.isMallApply()){
		note.getUserByApplyAgent().getAccounts().deductVoucher(note.getLogisticsFee(),"发货订单"+note.getOrderNo()+"扣减物流费用"+note.getLogisticsFee());
        }
        deliveryNoteDAO.save(note);
	}

	/**
	 * 代理商城直接发货订单
	 * @param note
	 * @param consigneeAgentCode
	 * @param cart
     */
	@Transactional
	public void buildDeliveryNoteByShopcart(DeliveryNote note,
			String consigneeAgentCode, ShopCart cart) {
		assembleAgent(consigneeAgentCode, note);
		try {
			cart.getItems()
					.values()
					.stream()
					.forEach(
							item -> {
								agentLevelTradingLimitedHandler.checkBalance(
										(Agent) note.getParentByOperator(),
										item.getGoods(), item.getQuantity());
							});
		} catch (BalanceNotEnoughException bne) {
			throw new BalanceNotEnoughException("上级代理"+note.getParentByOperator()+bne.getMessage()+"请通知及时补货");
		}
		Logistics logistics = logisticsDAO.findByProvinces(note.getProvince());
		note.applyFromMall(cart,logistics);//申请订单
		deliveryNoteDAO.save(note);
		cart.getItems().clear();
	}
	
	/**
	 * add by hf  通过官方商城下单	
	 * @param note
	 * @param consigneeAgentCode
	 * @param cart
	 */
	@Transactional
	public void buildDeliveryNoteByGmall(DeliveryNote note,
			String consigneeAgentCode, ShopCart cart) {
		assembleAgent(consigneeAgentCode, note);
		try {
			cart.getItems()
					.values()
					.stream()
					.forEach(
							item -> {
								agentLevelTradingLimitedHandler.checkBalance(
										note.getUserByApplyAgent().getParent(),
										item.getGoods(), item.getQuantity());
							});
		} catch (BalanceNotEnoughException bne) {
			throw new BalanceNotEnoughException("上级代理"+bne.getMessage()+"请通知及时补货");
		}
		note.applyFromGmall(cart);
		deliveryNoteDAO.save(note);
		cart.getItems().clear();
	}

	/**
	 * add by hf  通过特权商城下单 处理特权商城的上级
	 * @param note
	 * @param consigneeAgentCode
	 * @param cart
	 */
	@Transactional
	public void buildDeliveryNoteByTmall(DeliveryNote note,
										 String consigneeAgentCode, ShopCart cart) {
		assembleAgent(consigneeAgentCode, note);
		try {
			cart.getItems()
					.values()
					.stream()
					.forEach(
							item -> {
								agentLevelTradingLimitedHandler.checkBalance(
										note.getUserByApplyAgent().getTeqparent(),
										item.getGoods(), item.getQuantity());
							});
		} catch (BalanceNotEnoughException bne) {
			throw new BalanceNotEnoughException("特权供货商"+bne.getMessage()+"请通知及时补货");
		}
		Logistics logistics = logisticsDAO.findByProvinces(note.getProvince());
		note.applyFromTmall(cart,logistics);//申请订单
		deliveryNoteDAO.save(note);
		cart.getItems().clear();
	}

	/**
	 * add by hf  通过特权商城下单 自动确认
	 * @param note
	 * @param consigneeAgentCode
	 * @param cart
	 */
	@Transactional
	public void buildDeliveryNoteByTmallAndConfirm(DeliveryNote note,
										 String consigneeAgentCode, ShopCart cart,Boolean isUseVoucher) {
		assembleAgent(consigneeAgentCode, note);
		try {
			cart.getItems()
					.values()
					.stream()
					.forEach(
							item -> {
								agentLevelTradingLimitedHandler.checkBalance(
										(Agent) note.getParentByOperator(),
										item.getGoods(), item.getQuantity());
							});
		} catch (BalanceNotEnoughException bne) {
			throw new BalanceNotEnoughException("特权供货商"+bne.getMessage()+"请通知及时补货");
		}
		Logistics logistics = logisticsDAO.findByProvinces(note.getProvince());
		note.applyFromTmall(cart,logistics);//申请订单
            deliveryNoteAutoTransferToApplyAgent(note,isUseVoucher);//自动转货  扣减费用
            note.confirm();
            //上级确认的时候,此处扣减操作人员的物流费用  扣除物流费
            Agent agent =note.getUserByApplyAgent();
            agent.getAccounts().deductVoucher(note.getLogisticsFee(),"店铺直接发货"+note.getOrderNo()+"扣减物流费");
		    deliveryNoteDAO.save(note);
		cart.getItems().clear();
	}
	
	@Transactional
	public void modifyDeliveryNote(DeliveryNote note,
			String consigneeAgentCode, Integer[] quantitys, Integer[] goodsIds) {
		if (!note.isNew()) {
			throw new ApplicationException("只能修改新申请尚未处理的发货请求");
		}
		assembleAgent(consigneeAgentCode, note);
		note.cleanItems();
		Logistics logistics = logisticsDAO.findByProvinces(note.getProvince());
		note.addItems(assembleGoods(goodsIds), quantitys,logistics);
		deliveryNoteDAO.merge(note);
	}

	private List<Goods> assembleGoods(Integer[] ids) {
		List<Goods> goodses = new ArrayList<Goods>();
		for (Integer id : ids) {
			goodses.add(goodsDAO.findById(id));
		}
		return goodses;
	}

	private void assembleAgent(String consigneeAgentCode, DeliveryNote note) {
		if (Objects.nonNull(consigneeAgentCode)
				&& !consigneeAgentCode.trim().isEmpty()) {
			Agent agent = agentDAO.findByAgentCode(consigneeAgentCode);
			if (Objects.isNull(agent)) {
				throw new DataNotFoundException("代理编号对应的代理不存在");
			}
			//随意发货
//			if(!agent.isMyParent(note.getUserByApplyAgent()) && !Objects.equals(agent, note.getUserByApplyAgent())){
//				throw new DataNotFoundException("收货人必须为申请人或申请人的下级代理");
//			}
			//未确定到底是只可以发给自己  还是都可以发
			if(note.getConsigneeName()==null||"".equals(note.getConsigneeName())){
				note.setConsigneeName(agent.getRealName());
			}
			
			note.setUserByConsignee(agent);
		}else{
			throw new DataNotFoundException("必须输入收货人代理编号");
		}
	}


	@Transactional
	public void removeDeliveryNote(DeliveryNote note) {
		if (!note.isNew()) {
			throw new ApplicationException("只能删除新申请尚未处理的发货请求");
		}
        if (!note.isMallApply()) {//店铺下单的时候 并没有扣除物流费
            note.getUserByApplyAgent().getAccounts().increaseVoucher(note.getLogisticsFee(),"发货订单"+note.getOrderNo()+"物流费退回");
        }
		deliveryNoteDAO.delete(note);
	}
//这里会出现事务问题？
	@Transactional
	public void confirm(DeliveryNote note) {
		if (note.isMallApply()||note.isTmallApply()) {
			deliveryNoteAutoTransferToApplyAgent(note);
		}
		if (note.isSystemApply()) {
			note.getDeliveryItems()
					.stream()
					.forEach(
							item -> {
								agentLevelTradingLimitedHandler
										.checkTradingLimit(
												note.getUserByApplyAgent(),
												item.getGoods(), 0);
							});
		}
		note.confirm();
        //上级确认的时候,此处扣减操作人员的物流费用
        Agent parent =(Agent) note.getParentByOperator();
        if(!parent.isMutedUser())//上级不是公司,扣减物流
        parent.getAccounts().deductVoucher(note.getLogisticsFee(),"确定下级发货订单"+note.getOrderNo()+"扣减物流费");
        deliveryNoteDAO.merge(note);
	}

	/**
	 * 按照发货申请单自动转货
	 * 
	 * @param note
	 */
	@Transactional
	public void deliveryNoteAutoTransferToApplyAgent(DeliveryNote note) {
		//add by hf
//		if(note.isGmallApply()){//如果这个申请发货来源于官方商城
//			transfer.setApplyOrigin(TransferApplyOrigin.GMALL);
//		}else{
//			transfer.setApplyOrigin(TransferApplyOrigin.DELIVERY);//设置为直接发货
//		}
        //后面会根据transfer的来源进行不同的返利操作
        Transfer transfer = new Transfer();
        if (note.isTmallApply()){
            transfer.setApplyOrigin(TransferApplyOrigin.TMALL);
            transfer.setRemittance("特权店铺发出");
        }else {
            transfer.setApplyOrigin(TransferApplyOrigin.MALL);
            transfer.setRemittance("代理店铺发出");
        }
		//设置转货方为自己的上级
		transfer.setUserByFromAgent((Agent) note.getParentByOperator());
		Set<DeliveryItem> items = note.getDeliveryItems();
		Goods[] goods = new Goods[items.size()];
		Integer[] quantitys = new Integer[items.size()];
		Iterator<DeliveryItem> ite = items.iterator();
		int index = 0;
		while (ite.hasNext()) {
			DeliveryItem item = ite.next();
			goods[index] = item.getGoods();
			quantitys[index] = item.getQuantity();
			index++;
		}
		transfer.setUserByToAgent(note.getUserByApplyAgent());
		transferHandler.transferTo(transfer,
				goods, quantitys);
//		for (index = 0; index < goods.length; index++) {
//			agentLevelTradingLimitedHandler.checkTradingLimit(
//					note.getUserByApplyAgent(), goods[index], 0);
//		}
	}


    /**
     * 按照发货申请单自动转货 并且支付
     *
     * @param note
     */
    @Transactional
    public void deliveryNoteAutoTransferToApplyAgent(DeliveryNote note,Boolean isUseVoucher) {
        Transfer transfer = new Transfer();
        //add by hf
        if(note.isTmallApply()){//如果这个申请发货来源于特权
            transfer.setApplyOrigin(TransferApplyOrigin.TMALL);
            transfer.setRemittance("特权店铺发出");
        }else{
            transfer.setApplyOrigin(TransferApplyOrigin.MALL);//这就相当于主打转出
            transfer.setRemittance("代理店铺发出");
        }
        Agent userByToAgent=note.getUserByApplyAgent();
        Double amount=note.getAmount();

        Double advanceBalance=userByToAgent.getAccounts().getAdvanceBalance();
        Double advance_temp=amount>=advanceBalance?advanceBalance:amount;

        Double voucherBalance=userByToAgent.getAccounts().getVoucherBalance();
        Double voucher_temp=amount>=voucherBalance?voucherBalance:amount;
        if(!isUseVoucher){//不使用代金券
            //余额不足
            if(advance_temp<note.getAmount()){
                throw new ApplicationException("余额不足,请充值!");
            }
        }

        //余额不足
        if(advance_temp+voucher_temp<note.getAmount()){
            throw new ApplicationException("余额不足,请充值!");
        }

        transfer.setAdvance(advance_temp);//扣减可用的预存款
        //先扣减预存款
        userByToAgent.getAccounts().payAdvanceTo(note.getParentByOperator().getAccounts(), advance_temp);
        //设置需要支付的代金券
        BigDecimal b1= BigDecimal.valueOf(note.getAmount());
        BigDecimal b2= BigDecimal.valueOf(advance_temp);
        transfer.setVoucher(b1.subtract(b2).doubleValue());
        //再扣减代金券
        userByToAgent.getAccounts().payVoucherTo(note.getParentByOperator().getAccounts(), b1.subtract(b2).doubleValue());
        //设置转货方为自己的上级
        transfer.setUserByFromAgent((Agent) note.getParentByOperator());
        Set<DeliveryItem> items = note.getDeliveryItems();
        Goods[] goods = new Goods[items.size()];
        Integer[] quantitys = new Integer[items.size()];
        Iterator<DeliveryItem> ite = items.iterator();
        int index = 0;
        while (ite.hasNext()) {
            DeliveryItem item = ite.next();
            goods[index] = item.getGoods();
            quantitys[index] = item.getQuantity();
            index++;
        }
		transfer.setUserByToAgent(note.getUserByApplyAgent());
        transferHandler.transferTo(transfer,
                goods, quantitys);
        for (index = 0; index < goods.length; index++) {
            agentLevelTradingLimitedHandler.checkTradingLimit(
                    note.getUserByApplyAgent(), goods[index], 0);
        }
    }


	@Transactional
	public void delivery(DeliveryNote note,Double actual_logisticsFee) {
		if (note.isSystemApply() && note.isNew()) {
			note.confirm();
		}
		note.delivery();
		//实际需要收取的物流费<
		if(actual_logisticsFee<note.getLogisticsFee()){
            BigDecimal b1 = new BigDecimal(note.getLogisticsFee());
            BigDecimal b2 = new BigDecimal(actual_logisticsFee);
            Double temp=b1.subtract(b2).doubleValue();
            note.getUserByApplyAgent().getAccounts().increaseVoucher(temp,"物流费退回,订单"+note.getOrderNo());
			note.setLogisticsFee(actual_logisticsFee);//设置实际物流费
		}
		deliveryNoteDAO.merge(note);
	}

	@Transactional
	public void confirmAndDelivery(DeliveryNote note) {
		note.confirm();
		note.delivery();
		deliveryNoteDAO.merge(note);
	}

	@Autowired
	private AgentLevelTradingLimitedHandler agentLevelTradingLimitedHandler;

	@Autowired
	private DeliveryNoteDAO deliveryNoteDAO;
	@Autowired
	private TransferHandler transferHandler;
	@Autowired
	private DeliveryItemDAO deliveryItemDAO;
	@Autowired
	private TransferDAO transferDAO;
	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
    @Autowired
    private LogisticsDAO logisticsDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;

}
