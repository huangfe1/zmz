package com.dreamer.service.pmall.order;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.order.OrderItem;
import com.dreamer.domain.pmall.order.PaymentWay;
import com.dreamer.domain.user.User;
import com.dreamer.repository.mall.goods.MallGoodsDAO;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.pmall.order.OrderItemDao;
import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.util.ExcelFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.mx.otter.exception.ApplicationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderPayHandler {

	@Transactional
	public void pay(Order order, PaymentWay paymentWay, Double money) {
		if (!order.paymentRequired()) {
			throw new ApplicationException("此订单无需支付");
		}
		order.pay(paymentWay, money);
		fireEvent(order);
		deductGoodsStock(order);
		orderDAO.merge(order);
	}

    /**
     * 自动确认那些已经给钱但是没有显示支付的订单
     * @param orderNumber
     */
    @Transactional
    public void autoPay(String orderNumber){
//        try {
//            String fileName="/Users/huangfei/Desktop/order.xlsx";
//            ExcelFile excelFile = new ExcelFile();
//            String[] columns=new String[]{"交易时间","微信支付单号","商户号","特约商户号","设备号","appid","下单用户","交易场景","交易状态","支付成功时间","交易金额(元)"};
//            List<Map<String, Object>> lists= excelFile.read(fileName,0,1,columns);
//            for(Map<String,Object> map : lists){
//                System.out.println(map.get("商户订单号"));
//            }
////            orderDAO.autoPayNoPaid(orderNumber);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        try {
            String fileName="/Users/huangfei/Desktop/order.xlsx";
            ExcelFile excelFile = new ExcelFile();
            String[] columns=new String[]{"交易时间","微信支付单号","商户号","特约商户号","设备号","appid","下单用户","交易场景","交易状态","支付成功时间","交易金额(元)"};
            List<Map<String, Object>> lists= excelFile.read(fileName,0,1,columns);
            for(int i=0;i<lists.size();i++){
                Map<String,Object> map=lists.get(i);
                String orderNu=map.get(columns[1]).toString().replace("`","");
                System.out.println(i+"==="+orderNu);
            }
//            orderDAO.autoPayNoPaid(orderNumber);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

	/**
	 * 减少库存
	 * @param order
     */
	@Transactional
	public void deductGoodsStock(Order order){
		order.getItems().entrySet().stream().forEach(entry->{
			MallGoods goods=mallGoodsDAO.findById(entry.getKey());
			if(goods.getStockQuantity()>=entry.getValue().getQuantity())//库存足够的时候在减少库存,有些偏差
			goods.deductCurrentStock(entry.getValue().getQuantity());
		});
	}


	@Transactional
	public void back(OrderItem item) {
		orderItemDao.merge(item);
	}

//	private void fireEvent(Order order) {
//		Agent temp = order.getUser().getParent();
//		if(temp.isMutedUser()){
//			LOG.error("订单代理为直属代理,不向上返回代金券和福利积分");
//			return;
//		}
//		Integer voucherLevel = systemParameterDAO.getCouponsLevel();
//		LOG.error("代金券返还层级:{}", voucherLevel);
//		Integer benefitPointsLevel = systemParameterDAO.getBenefitPointsLevel();
//		LOG.error("福利积分返还层级:{}", benefitPointsLevel);
//		Double voucherPerLevel = (Objects.isNull(voucherLevel) || voucherLevel == 0) ? 0
//				: new BigDecimal(order.getVoucher() * 0.3).setScale(2,
//						BigDecimal.ROUND_HALF_UP).doubleValue();
//		LOG.error("每层级返代金券:{} 总返还:{}", voucherPerLevel, order.getVoucher());
//		Double benefitPointsPerLevel = (Objects.isNull(benefitPointsLevel) || benefitPointsLevel == 0) ? 0
//				: new BigDecimal(order.getBenefitPoints() * 0.3)
//						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		LOG.error("每层级返福利积分:{} 总返还:{}", benefitPointsPerLevel,
//				order.getBenefitPoints());
//
//		for (int level = 0; level < voucherLevel; level++) {
//			String more=order.getUser().getRealName()+"在积分商城购物，订单编号"+order.getOrderNo();
//			temp.getAccounts().increaseVoucher(new Double(voucherPerLevel),more);
//			LOG.debug("准备进入返还第{}层代理{}代金券", level, voucherPerLevel);
//			if (temp.isTopAgent() || Objects.isNull(temp.getParent())) {
//				LOG.error("已经到了最上级代理或上级代理为空,不再上溯返还代金券");
//				break;
//			} else {
//				temp = temp.getParent();
//			}
//		}
//		temp = order.getUser().getParent();
//		for (int level = 0; level < benefitPointsLevel; level++) {
//			temp.getAccounts().increaseBenefitPoints(
//					new Double(benefitPointsPerLevel));
//			if (temp.isTopAgent() || Objects.isNull(temp.getParent())) {
//				LOG.error("已经到了最上级代理或上级代理为空,不再上溯返还福利积分");
//				break;
//			} else {
//				temp = temp.getParent();
//			}
//		}
//	}

	private void fireEvent(Order order) {
//		Agent temp = order.getUser().getParent();
//		if(temp.isMutedUser()){
//			LOG.error("订单代理为直属代理,不向上返回代金券和福利积分");
//			return;
//		}
//
//		Integer benefitPointsLevel = systemParameterDAO.getBenefitPointsLevel();
//		LOG.error("福利积分返还层级:{}", benefitPointsLevel);
//		Double benefitPointsPerLevel = (Objects.isNull(benefitPointsLevel) || benefitPointsLevel == 0) ? 0
//				: new BigDecimal(order.getBenefitPoints() * 0.3)
//				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		LOG.error("每层级返福利积分:{} 总返还:{}", benefitPointsPerLevel,
//				order.getBenefitPoints());
//		temp = order.getUser().getParent();
//		for (int level = 0; level < benefitPointsLevel; level++) {
//			temp.getAccounts().increaseBenefitPoints(
//					new Double(benefitPointsPerLevel));
//			if (temp.isTopAgent() || Objects.isNull(temp.getParent())) {
//				LOG.error("已经到了最上级代理或上级代理为空,不再上溯返还福利积分");
//				break;
//			} else {
//				temp = temp.getParent();
//			}
//		}
        //订单的每个货物都返利
        order.getItems().values().stream().forEach(p->fireVoucher(p,order.getUser()));

			}

    /**
     * 返还代金券
     */
    private void fireVoucher(OrderItem item, User user){
//        String more = user.getRealName() + "积分商城购买了" + item.getGoodsName() + "货物" + item.getQuantity() + "" + item.getGoodsSpec();//转账备注
		StringBuilder more=new StringBuilder(user.getRealName());
		more.append( "积分商城购买了").append(item.getGoodsName()).append("货物").append(item.getQuantity()).append(item.getGoodsSpec());
        Double[] vs=getbackVouchers(item.getVouchers());//返利模式
        User parent=user.getParent();//获取上家
		Double  v;
        for(int i=0;i<vs.length;i++){
            if(parent.isMutedUser()){break;}//如果上家是公司了 就不用返回
//                Double v=vs[i]*item.getQuantity();//乘以数量
			   v=new BigDecimal(vs[i]*item.getQuantity()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//精确度
                parent.getAccounts().increaseVoucher(v,more.toString());
                parent=parent.getParent();//上家的上家
        }
    }

    /**
     * 获取返利模式
     *
     * @return
     */
    public Double[] getbackVouchers(String str) {
        String[] strs = str.trim().split("_");
        Double[] temps = new Double[strs.length];
        for (int i = 0; i < temps.length; i++) {
            temps[i] = Double.parseDouble(strs[i]);
            strs[i]=null;//回收
        }
        return temps;
    }


	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private MallGoodsDAO mallGoodsDAO;
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
