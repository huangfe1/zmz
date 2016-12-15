package com.dreamer.service.goods;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.repository.goods.DeliveryItemDAO;
import com.dreamer.repository.goods.TransferDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.BalanceNotEnoughException;
import ps.mx.otter.exception.ExceedsMaximumLimitException;

import java.util.Objects;

@Service
public class AgentLevelTradingLimitedHandler {

	/**
	 * 检查当前代理交易总量是否超过等级阈值
	 * <p>交易总量=申请发货量+转出货物量(主动转出+下级申请转出)</p>
	 * @param fromAgent
	 * @param goods
	 * @param quantity
	 */
	public void checkTradingLimit(Agent fromAgent,Goods goods,Integer quantity){
		if(fromAgent.isMutedUser()){
			return;
		}
		AgentLevel level=fromAgent.getGoodsLowestAgentLevel(goods);
		AgentLevel parentLevel=level.getParent();
		if(Objects.isNull(parentLevel)){
			return;
		}
		Integer limited=goods.getThreshold(parentLevel);
		/*//指定商品已经向转出人申请总数
		Integer effectiveTransferQuantity=transferDAO.sumAllEffectiveTransferQuantity(fromAgent.getId(),goods.getId()).intValue();
		//转出人所有有效申请发货数
		Integer effectiveDeliveryQuantity=deliveryItemDAO.sumEffectiveDeliveryQuantity(fromAgent.getId(), goods.getId()).intValue();*/
		Integer cumulant=this.sumTradingCumulative(fromAgent, goods);
		if(Integer.compare(limited, cumulant+quantity)<=0){
			throw new ExceedsMaximumLimitException("您等级为:"+level.getName()+" "+goods.getName()+"产品限制转出:"+limited+"盒 需向公司进行等级认证");
		}
		
	}
	
	
	
	/**
	 * <h3>账户余额判定</h3>
	 * <p>账户余额是否足够=当前余额-(下级已向代理发起的未处理转货数+代理向公司发起的发货申请数+本次操作数)>=0
	 * @param agent 代理 转货操作时传入的应当是转出方代理,发货操作时是申请发货的代理
	 * @param goods 商品
	 * @param quantity 数量 本次操作数
	 */
	public void checkBalance(Agent agent,Goods goods,Integer quantity){
		//下级已经向代理发起的尚未处理的转货申请数
		Integer alreadyApplyTransferQuantity=transferDAO.sumNoConfirmApplyQuantity(agent.getId(), goods.getId()).intValue();
		//代理已经申请的未处理发货数或者自己已经申请的退货
		Integer alreadyApplyQuantity=deliveryItemDAO.sumNoConfirmNewApplyQuantity(agent.getId(), goods.getId()).intValue();
		//申请发货人可用余额=当前余额-(申请人已经系统申请的发货未处理数+下级向申请人发起的转货未处理数)
		if(agent.isBalanceNotEnough(goods,quantity+alreadyApplyQuantity+alreadyApplyTransferQuantity)){
			throw new BalanceNotEnoughException(goods.getName()+" 账户余额不足,申请的发货/退货未处理数"+alreadyApplyQuantity+",转货未处理数"+alreadyApplyTransferQuantity);
		}
	}
	
	/**
	 * 转出代理和转入代理之间的指定产品的当前所处等级比较
	 * <p>转出代理等级不能低于转入代理等级</p>
	 * @param fromAgent
	 * @param toAgent
	 * @param goods
	 */
	public void checkAgentLevel(Agent fromAgent,Agent toAgent,Goods goods){
		if(fromAgent.isMutedUser()){
			return;
		}
		GoodsAccount fromGac=fromAgent.loadAccountForGoodsNotNull(goods);
		GoodsAccount toGac=toAgent.loadAccountForGoodsNotNull(goods);
		if(fromGac.getAgentLevel().higherThanMe(toGac.getAgentLevel())){
			throw new ApplicationException("产品"+goods.getName()+"转出方产品等级不能低于转入方等级");
		}
	}
	
	/**
	 * <p>指定代理的指定商品的累计出货量,是以下几项之和:</p>
	 * <ul>
	 * <li>下级向指定代理已申请的该商品转货数(包括新申请尚未确认的转货数)</li>
	 * <li>指定代理已经向公司申请的该商品发货数(包括新申请尚未确认的发货数)</li>
	 * <li>减去代理接收的下级的退货数</li>
	 * </ul>
	 * @param fromAgent 指定代理
	 * @param goods 指定商品
	 * @return Integer 累计出货量
	 */
	public Integer sumTradingCumulative(Agent fromAgent,Goods goods){
		//指定商品已经向转出人申请的总数(包括已申请、已确认的)
		Integer effectiveTransferQuantity=transferDAO.sumAllEffectiveTransferQuantity(fromAgent.getId(),goods.getId()).intValue();
		//指定商品退给指定代理的数量
		Integer backTransferQuantity=transferDAO.sumAllEffectiveBackTransferQuantity(fromAgent.getId(), goods.getId()).intValue();
		//转出人所有有效申请发货数
		Integer effectiveDeliveryQuantity=deliveryItemDAO.sumEffectiveDeliveryQuantity(fromAgent.getId(), goods.getId()).intValue();
		Integer cumulant=effectiveTransferQuantity+effectiveDeliveryQuantity-backTransferQuantity;
		return Objects.isNull(cumulant)||cumulant<0?0:cumulant;		
	}
	
	
	@Autowired
	private TransferDAO transferDAO;
	
	@Autowired
	private DeliveryItemDAO deliveryItemDAO;
}
