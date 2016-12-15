package com.dreamer.view.goods;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Transfer;
import com.dreamer.domain.goods.TransferApplyOrigin;
import com.dreamer.domain.mall.shopcart.CartItem;
import com.dreamer.domain.mall.shopcart.ShopCart;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevelName;
import com.dreamer.domain.user.MutedUser;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.goods.TransferDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.service.goods.TransferHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/transfer")
public class TransferController {
	/**
	 * 后台申请发货
	 * @param transfer
	 * @param goodsIds
	 * @param quantitys
	 * @param model
	 * @param request
     * @return
     */
	@RequestMapping(value = "/applyFrom.json", method = RequestMethod.POST)
	public Message applyTransfer(
			@ModelAttribute("parameter") Transfer transfer, Integer[] goodsIds,
			Integer[] quantitys, Model model, HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(user.getId());
			if (user.isTopAgent()) {
				MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
						request, Constant.MUTED_USER_KEY);
				transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
						.getId()));
			} else {
				transfer.setUserByFromAgent((Agent) agent.getParent());//由上级处理
		}
			transfer.setUserByToAgent(agent);
			if (goodsIds == null || goodsIds.length == 0) {
				throw new ApplicationException("未指定申请转货的货物");
			}
			transferHandler.applyTransfer(transfer, goodsIds, quantitys);
			return Message.createSuccessMessage("ok","转货申请提交成功");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("转货申请失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}




    /**
	 * 后台结算
	 * @param transfer
	 * @param goodsIds
	 * @param quantitys
	 * @param model
	 * @param request
     * @return
     */
	@RequestMapping(value = "/calculate.json", method = RequestMethod.POST)
	public Message calculate(
			@ModelAttribute("parameter") Transfer transfer, Integer[] goodsIds,
			Integer[] quantitys, Model model, HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(user.getId());
			if (user.isTopAgent()) {
				MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
						request, Constant.MUTED_USER_KEY);
				transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
						.getId()));
			} else {
				transfer.setUserByFromAgent((Agent) agent.getParent());//改变上家
			}
			transfer.setUserByToAgent(agent);
			if (goodsIds == null || goodsIds.length == 0) {
				throw new ApplicationException("未指定申请转货的货物");
			}
			Transfer result=transferHandler.calculate(transfer, goodsIds, quantitys);
			WebUtil.addSessionAttribute(request, "transfer_calculate", result);
			return Message.createSuccessMessage("ok","calculate");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("转货申请价格核算失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

    /**
     * 主动转货 退回上家 操作
     * @param transfer
     * @param goodsIds
     * @param quantitys
     * @param toBack 0转货 1退货
     * @param request
     * @return
     */
	@RequestMapping(value = "/to.json", method = RequestMethod.POST)
	public Message transferTo(@ModelAttribute("parameter") Transfer transfer,
			Integer[] goodsIds, Integer[] quantitys,
			@RequestParam(value="toback",required=false) Integer toBack,
			HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if(Objects.equals(toBack, 1)){//退货申请操作
                transferHandler.applyBackTransfer(transfer,goodsIds,quantitys,user);
			}else{                        //主动转出操作
                transferHandler.transferTo(transfer, goodsIds, quantitys,user);
			}
			return Message.createSuccessMessage("货物转出成功");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("转出货物失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

    /**
     * 确认转货
     * @param transfer
     * @param request
     * @return
     */
	@RequestMapping(value = "/confirm.json", method = RequestMethod.POST)
	public Message confirm(@ModelAttribute("parameter") Transfer transfer,
			HttpServletRequest request) {
		try {
			if(transfer.isBackedTransfer()){//退货
                transferHandler.confirmBackTransfer(transfer);
                return Message.createSuccessMessage("货物退还确认成功");
			}else {
                transferHandler.confirmTransfer(transfer);
                return Message.createSuccessMessage("货物转出确认成功");
            }

		} catch (ApplicationException exp) {
			exp.printStackTrace();
			LOG.error("确认失败"+exp.getMessage(), exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/refuse.json", method = RequestMethod.POST)
	public Message refuse(@ModelAttribute("parameter") Transfer transfer,
			HttpServletRequest request) {
		try {
			//User user = (User) WebUtil.getCurrentUser(request);
			transferHandler.refuseTransfer(transfer);
			return Message.createSuccessMessage("拒绝货物转出成功");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("拒绝转出货物失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
	@RequestMapping(value = "/remove.json", method = {RequestMethod.POST,RequestMethod.DELETE})
	public Message remove(@ModelAttribute("parameter") Transfer transfer,
			HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Integer operaterId =transfer.getUserByToAgent().getId();
			if(transfer.isBackedTransfer()){//如果是退货,操作人就是转出方
				operaterId=transfer.getUserByFromAgent().getId();
			}
			if(Objects.equals(operaterId,user.getId())){//撤销只能撤销自己的
				transferHandler.removeTransfer(transfer);
			}else{
				throw new ApplicationException("只能撤销本人申请的转货");
			}
			return Message.createSuccessMessage("撤销转货申请成功");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("拒绝转出货物失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public Transfer preprocess(@RequestParam("id") Optional<Integer> id) {
		Transfer parameter = new Transfer();
		if (id.isPresent()) {
			parameter = transferDAO.findById(id.get());
		} else {
			parameter = new Transfer();
		}
		return parameter;
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private TransferDAO transferDAO;
	@Autowired
	private TransferHandler transferHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
/**
 * add by hxf----------------------------------------------------------------------------------------
 */

	
	@RequestMapping(value = "/hf_confirm.json", method = RequestMethod.POST)
	public Message hf_confirm(@ModelAttribute("parameter") Transfer transfer,
			HttpServletRequest request) {
		try {
			transferHandler.confirmTransfer(transfer);
			return Message.createSuccessMessage("货物转出确认成功");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("撤销转货申请失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}



/**
 * mall新增从购物车转货！！
 */
	
	private static final String CART = "shopcart";
	
	@RequestMapping(value = "/hf_applyFrom.json", method = RequestMethod.POST)
	public Message hf_applyTransfer(
			@ModelAttribute("parameter") Transfer transfer,  Model model, HttpServletRequest request) {
		ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
				CART);
		
		
		
		Integer[] goodsIds=null;
				
		Integer[] quantitys=null;
		//初始化
		if(!cart.getItems().isEmpty()){
			goodsIds=new Integer[cart.getItems().size()];
			quantitys=new Integer[cart.getItems().size()];
		}
		//封装赋值
		int i=0;
		for(Entry<Integer, CartItem> entry:cart.getItems().entrySet()){    
			goodsIds[i]=entry.getKey();
			quantitys[i]=entry.getValue().getQuantity();
			i++;
		}   
		
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(user.getId());
			if (user.isTopAgent()) {
				MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
						request, Constant.MUTED_USER_KEY);
				transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
						.getId()));
			} else {
				//transfer.setUserByFromAgent((Agent) agent.getParent());//改变上家
				GoodsAccount gac=agent.loadAccountForGoodsId(16);//主打产品
				String lv=gac.getAgentLevel().getName();
				if(lv.contains(AgentLevelName.官方.toString())||lv.contains(AgentLevelName.联盟单位.toString())||lv.contains(AgentLevelName.发起者.toString())){//订单由官方处理
					transfer.setUserByFromAgent(agentDAO.findById(3));
				}else{
					transfer.setUserByFromAgent(agent.getParent());//改变上家
				}
			}
			transfer.setUserByToAgent(agent);transfer.setApplyOrigin(TransferApplyOrigin.MALL);
			if (goodsIds == null || goodsIds.length == 0) {
				throw new ApplicationException("未指定申请转货的货物");
			}
            transferHandler.applyTransfer(transfer, goodsIds, quantitys);
			//清空购物车
			cart.getItems().clear();
			
			return Message.createSuccessMessage("ok","转货申请提交成功");
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("转货申请失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}
	
//	/**
//	 * gmall新增从购物车转货！！
//	 */
//	private static final String GCART = "gshopcart";
//
//		@RequestMapping(value = "/hf_gmallApplyFrom.json", method = RequestMethod.POST)
//		public Message hf_gmallApplyTransfer(
//				@ModelAttribute("parameter") Transfer transfer,  Model model, HttpServletRequest request) {
//			ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
//					GCART);
//
//
//
//			Integer[] goodsIds=null;
//
//			Integer[] quantitys=null;
//			//初始化
//			if(!cart.getItems().isEmpty()){
//				goodsIds=new Integer[cart.getItems().size()];
//				quantitys=new Integer[cart.getItems().size()];
//			}
//			//封装赋值
//			int i=0;
//			for(Entry<Integer, CartItem> entry:cart.getItems().entrySet()){
//				goodsIds[i]=entry.getKey();
//				quantitys[i]=entry.getValue().getQuantity();
//				i++;
//			}
//
//			try {
//				User user = (User) WebUtil.getCurrentUser(request);
//				Agent agent = agentDAO.findById(user.getId());
//				if (user.isTopAgent()) {
//					MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
//							request, Constant.MUTED_USER_KEY);
//					transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
//							.getId()));
//				} else {
//					//transfer.setUserByFromAgent((Agent) agent.getParent());//改变上家
//					GoodsAccount gac=agent.loadAccountForGoodsId(16);//主打产品
//					String lv=gac.getAgentLevel().getName();
//					//官方商城购物都是公司处理
//						transfer.setUserByFromAgent(agentDAO.findById(3));
//				}
//				transfer.setApplyOrigin(TransferApplyOrigin.GMALL);//设置为官方商城的申请
//				transfer.setUserByToAgent(agent);
//				if (goodsIds == null || goodsIds.length == 0) {
//					throw new ApplicationException("未指定申请转货的货物");
//				}
//				transferHandler.applyTransfer(transfer, goodsIds, quantitys);
//				//清空购物车
//				cart.getItems().clear();
//
//				return Message.createSuccessMessage("ok","转货申请提交成功");
//			} catch (Exception exp) {
//				exp.printStackTrace();
//				LOG.error("转货申请失败", exp);
//				return Message.createFailedMessage(exp.getMessage());
//			}
//		}


    /**
     * 获取支付方式
     * @param request
     * @param model
     * @return
     */
    /**
     * 获取支付方式
     */
    @RequestMapping(value = "/payWay.json",method = RequestMethod.POST)
    public Message getPayWay(HttpServletRequest request,Model model){
        Message msg= Message.createSuccessMessage("ok","成功");
        User user =(User) WebUtil.getCurrentUser(request);
        Agent agent=agentDAO.findById(user.getId());
		//获取任意一款产品
		if(!agent.isTeqVip())
            msg.setMessage("hide");
        return msg;
    }

		
		/**
		 * tmall新增从购物车转货！！
		 */
		private static final String TCART = "tshopcart";
			
			@RequestMapping(value = "/hf_tmallApplyFrom.json", method = RequestMethod.POST)
			public Message hf_mallApplyTransfer(
					@ModelAttribute("parameter") Transfer transfer,  Model model, HttpServletRequest request,@RequestParam(required = false)Boolean useAdvance) {
				ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
						TCART);
				
				
				
				Integer[] goodsIds=null;
						
				Integer[] quantitys=null;
				//初始化
				if(!cart.getItems().isEmpty()){
					goodsIds=new Integer[cart.getItems().size()];
					quantitys=new Integer[cart.getItems().size()];
				}
				//封装赋值
				int i=0;
				for(Entry<Integer, CartItem> entry:cart.getItems().entrySet()){    
					goodsIds[i]=entry.getKey();
					quantitys[i]=entry.getValue().getQuantity();
					i++;
				}   
				
				try {
					User user = (User) WebUtil.getCurrentUser(request);
					Agent agent = agentDAO.findById(user.getId());
					if (user.isTopAgent()) {
						MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
								request, Constant.MUTED_USER_KEY);
						transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
								.getId()));
					} else {
						//transfer.setUserByFromAgent((Agent) agent.getParent());//改变上家
//						GoodsAccount gac=agent.loadAccountForGoodsType(GoodsType.TEQ);//任意一个特权授权的产品  代表所有
//						String lv=gac.getAgentLevel().getName();
//						if(lv.contains(AgentLevelName.大区.toString())||lv.contains(AgentLevelName.联盟单位.toString())){//订单由官方处理
//							transfer.setUserByFromAgent(agentDAO.findById(3));
//						}else{
//							transfer.setUserByFromAgent(agent.getParent());//改变上家
//						}
						if(agent.isTeqVip()){//如果是筑美的特权商城VIP
							transfer.setUserByFromAgent(agentDAO.findById(3));
						}else{
							transfer.setUserByFromAgent(agent.getTeqparent());//设置为自己的特权上级
						}
					}
					transfer.setApplyOrigin(TransferApplyOrigin.TMALL);//设置为特权商城的申请
					transfer.setUserByToAgent(agent);
					if (goodsIds == null || goodsIds.length == 0) {
						throw new ApplicationException("未指定申请转货的货物");
					}
//                    //先申请,后支付
//                    transferHandler.applyTransfer(transfer, goodsIds, quantitys);

					if(useAdvance!=null&&useAdvance){//如果使用预存款
                        transferHandler.confirmByAdvance(transfer, goodsIds, quantitys);
					}else {
                        transferHandler.applyTransfer(transfer, goodsIds, quantitys);
                    }
					//清空购物车
					cart.getItems().clear();
					return Message.createSuccessMessage("ok","转货申请提交成功");
				} catch (Exception exp) {
					exp.printStackTrace();
					LOG.error("转货申请失败", exp);
					return Message.createFailedMessage(exp.getMessage());
				}
			}
			
		
	
	/**
	 * 新增从购物车结算！！
	 */
	
	@RequestMapping(value = "/hf_calculate.json", method = RequestMethod.POST)
	public Message hf_calculate(
			@ModelAttribute("parameter") Transfer transfer, Model model, HttpServletRequest request,String type) {
		
		Integer[] goodsIds=null;
		
		Integer[] quantitys=null;
		
		ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
				CART);
	
		
		//初始化
		if(cart!=null&&!cart.getItems().isEmpty()){
			goodsIds=new Integer[cart.getItems().size()];
			quantitys=new Integer[cart.getItems().size()];
		}
		//封装赋值
		int i=0;
		for(Entry<Integer, CartItem> entry:cart.getItems().entrySet()){    
			goodsIds[i]=entry.getKey();
			quantitys[i]=entry.getValue().getQuantity();
			i++;
		}   
		i=0;
		Message msg=Message.createSuccessMessage("ok","calculate");
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(user.getId());
			if (user.isTopAgent()) {
				MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
						request, Constant.MUTED_USER_KEY);
				transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
						.getId()));
			} else {
				
				GoodsAccount gac=agent.loadAccountForGoodsId(16);//主打产品
				String lv=gac.getAgentLevel().getName();
				if(lv.contains(AgentLevelName.发起者.toString())||lv.contains(AgentLevelName.联盟单位.toString())||lv.contains(AgentLevelName.官方.toString())){//分公司跟发起者的订单由公司处理
					transfer.setUserByFromAgent(agentDAO.findById(3));
					msg.setData("hide");
				}else{
					transfer.setUserByFromAgent((Agent) agent.getParent());//改变上家
				}
			}
			transfer.setUserByToAgent(agent);
			if (goodsIds == null || goodsIds.length == 0) {
				throw new ApplicationException("未指定申请转货的货物");
			}
			Transfer result=transferHandler.calculate(transfer, goodsIds, quantitys);
			WebUtil.addSessionAttribute(request, "transfer_calculate", result);
			return msg;
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("转货申请价格核算失败", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	
//	/**
//	 * 新增从官方商城购物车结算！！
//	 */
//
//	@RequestMapping(value = "/hf_gmallCalculate.json", method = RequestMethod.POST)
//	public Message hf_gmallCalculate(
//			@ModelAttribute("parameter") Transfer transfer, Model model, HttpServletRequest request) {
//
//		Integer[] goodsIds=null;
//
//		Integer[] quantitys=null;
//
//		ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
//				GCART);
//
//
//		//初始化
//		if(cart!=null&&!cart.getItems().isEmpty()){
//			goodsIds=new Integer[cart.getItems().size()];
//			quantitys=new Integer[cart.getItems().size()];
//		}
//		//封装赋值
//		int i=0;
//		for(Entry<Integer, CartItem> entry:cart.getItems().entrySet()){
//			goodsIds[i]=entry.getKey();
//			quantitys[i]=entry.getValue().getQuantity();
//			i++;
//		}
//		i=0;
//
//		try {
//			User user = (User) WebUtil.getCurrentUser(request);
//			Agent agent = agentDAO.findById(user.getId());
//			if (user.isTopAgent()) {
//				MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
//						request, Constant.MUTED_USER_KEY);
//				transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
//						.getId()));
//			} else {
//
//				GoodsAccount gac=agent.loadAccountForGoodsId(16);//主打产品
//					transfer.setUserByFromAgent(agentDAO.findById(3));
//			}
//			transfer.setUserByToAgent(agent);
//			if (goodsIds == null || goodsIds.length == 0) {
//				throw new ApplicationException("未指定申请转货的货物");
//			}
//			Transfer result=transferHandler.calculate(transfer, goodsIds, quantitys);
//			WebUtil.addSessionAttribute(request, "transfer_calculate", result);
//			return Message.createSuccessMessage("ok","calculate");
//		} catch (Exception exp) {
//			exp.printStackTrace();
//			LOG.error("转货申请价格核算失败", exp);
//			return Message.createFailedMessage(exp.getMessage());
//		}
//	}


	
	/**
	 * 新增从特权商城购物车结算！！
	 */
	
	@RequestMapping(value = "/hf_tmallCalculate.json", method = RequestMethod.POST)
	public Message hf_tmallCalculate(
			@ModelAttribute("parameter") Transfer transfer, Model model, HttpServletRequest request) {
		
		Integer[] goodsIds=null;
		
		Integer[] quantitys=null;
		
		ShopCart cart = (ShopCart) WebUtil.getSessionAttribute(request,
				TCART);
	
		
		//初始化
		if(cart!=null&&!cart.getItems().isEmpty()){
			goodsIds=new Integer[cart.getItems().size()];
			quantitys=new Integer[cart.getItems().size()];
		}
		//封装赋值
		int i=0;
		for(Entry<Integer, CartItem> entry:cart.getItems().entrySet()){    
			goodsIds[i]=entry.getKey();
			quantitys[i]=entry.getValue().getQuantity();
			i++;
		}   
		i=0;
		Message msg=Message.createSuccessMessage("ok","calculate");
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(user.getId());
			if (user.isTopAgent()) {
				MutedUser mutedUser = (MutedUser) WebUtil.getSessionAttribute(
						request, Constant.MUTED_USER_KEY);
				transfer.setUserByFromAgent(mutedUserDAO.findById(mutedUser
						.getId()));
			} else {
				
//				GoodsAccount gac=agent.loadAccountForGoodsType(GoodsType.TEQ);//任意一个特权授权的等级  代表所有
//				String lv=gac.getAgentLevel().getName();
//				//大区及以上等级显示
//				if(lv.contains(AgentLevelName.大区.toString())||lv.contains(AgentLevelName.联盟单位.toString())||lv.contains(AgentLevelName.董事.toString())||lv.contains(AgentLevelName.金董.toString())){//订单由官方处理
				if(agent.isTeqVip()){//如果是特权vip
                transfer.setUserByFromAgent(agentDAO.findById(3));
				}else{
					msg.setMessage("hide");
                    transfer.setUserByFromAgent(agent.getTeqparent());//改变特权上家
				}
				
			}
			transfer.setUserByToAgent(agent);
			if (goodsIds == null || goodsIds.length == 0) {
				throw new ApplicationException("未指定申请转货的货物");
			}
			Transfer result=transferHandler.calculate(transfer, goodsIds, quantitys);
			WebUtil.addSessionAttribute(request, "transfer_calculate", result);
			return msg;
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("转货申请价格核算失败/可能没有响应的授权", exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	

}
