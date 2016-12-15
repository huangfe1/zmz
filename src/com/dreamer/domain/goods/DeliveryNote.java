package com.dreamer.domain.goods;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.date.DateUtil;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.mall.shopcart.CartItem;
import com.dreamer.domain.mall.shopcart.ShopCart;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;

public class DeliveryNote implements java.io.Serializable {

	private static final long serialVersionUID = -3044942461408586812L;
	private Integer id;
	private Integer version;
	private String orderNo;
	private User userByConsignee;
	private User userByOperator;
    private User parentByOperator;
	private Agent userByApplyAgent;
	private String mobile;
	private String province;
    private String city;
    private String county;
	private String address;
	private String remark;
	private DeliveryStatus status;
	private String logisticsCode;
	private Double logisticsFee;
	private String logistics;
	private Boolean printStatus;
	private Timestamp updateTime;
	private Date applyTime;
	private Date deliveryTime;
	private String consigneeName;
	private DeliveryApplyOrigin applyOrigin;
	private Set <DeliveryItem> deliveryItems = new HashSet<DeliveryItem>(0);
	public Double getAmount(){
		Double amount=deliveryItems.stream().mapToDouble(d->{
			return d.getAmount();
		}).sum();
		BigDecimal p=new BigDecimal(amount);
		return p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}


	public boolean isMallApply(){
		return applyOrigin==DeliveryApplyOrigin.MALL;
	}

    public boolean isTmallApply(){
        return applyOrigin==DeliveryApplyOrigin.TMALL;
    }

	public boolean isSystemApply(){
		return applyOrigin==DeliveryApplyOrigin.SYSTEM;
	}
	//add by hf
	public boolean isGmallApply(){
		return applyOrigin==DeliveryApplyOrigin.GMALL;
	}

	public void mallApply(){
		applyOrigin=DeliveryApplyOrigin.MALL;
	}

	/**
	 * 管理后台发起的发货申请
	 * @param goods
	 * @param quantitys
	 * @return
	 */
	public DeliveryNote apply(List<Goods> goods,Integer[]quantitys,Logistics logistics){
		applyOrigin=DeliveryApplyOrigin.SYSTEM;
        Double fee=addItems(goods,quantitys,logistics);
        setLogisticsFee(fee);//获取物流费
		changeToApplyStatus();
		fillOrderNo();
		return this;
	}

    /**
     *获取物流费核心算法
     * @param weight 重量
     * @param province 身份
     * @return
     */
    public Double getLogisticsFee(Double weight,Logistics logistics) {
        weight=Math.ceil(weight);
        String[] weights = logistics.getWeights().trim().split("_");
        String[] prices = logistics.getPrices().trim().split("_");
        Double temp_price = 0.0;
        for (int i = 0; i < weights.length - 1; i++) {
            Double temp_weight = Double.parseDouble(weights[i]);
            if (weight <= temp_weight){//小于第一区间
                temp_price = Double.parseDouble(prices[i]);
                break;
            }
        }
            if(temp_price==0.0){//如果不在区间内
                Double first_weight =Double.parseDouble(weights[weights.length-1]);
                Double first_price=Double.parseDouble(prices[prices.length-2]);
                Double per_price=Double.parseDouble(prices[prices.length-1]);
                temp_price=first_price+(weight-first_weight)*per_price;
            }
                return temp_price;
            }
	private Double getLogisticsFee(Goods goods,Integer amount,Logistics logistics){
			Double boxfee=getLogisticsFee(goods.getBoxamount()*goods.getWeight(),logistics);
			Integer box = amount/goods.getBoxamount();
            Integer retail = amount%goods.getBoxamount();
            return retail==0 ? box*boxfee : box*boxfee+getLogisticsFee(retail*goods.getWeight(),logistics);
			}



	/**
	 * 从购物车发起的发货申请
	 * @param cart
	 * @return
	 */
	public DeliveryNote applyFromMall(ShopCart cart,Logistics logistics){//申请订单
		mallApply();
		Map<Integer,CartItem> items=cart.getItems();
		Iterator<Integer> ite=items.keySet().iterator();
        Double fee=0.0;
		while(ite.hasNext()){
			CartItem item=items.get(ite.next());
			DeliveryItem ditem=buildItem(item.getGoods(),item.getQuantity(),item.getPrice());
			addItem(ditem);
            fee+=getLogisticsFee(item.getGoods(),item.getQuantity(),logistics);
		}
        setLogisticsFee(fee);//获取物流费
		changeToApplyStatus();
		fillOrderNo();
		return this;
	}

	/**add bg hf
	 * 从官方商城发起的发货
	 * @param cart
	 * @return
	 */
	public DeliveryNote applyFromGmall(ShopCart cart){
		applyOrigin=DeliveryApplyOrigin.GMALL;
		Map<Integer,CartItem> items=cart.getItems();
		Iterator<Integer> ite=items.keySet().iterator();
		while(ite.hasNext()){
			CartItem item=items.get(ite.next());
			DeliveryItem ditem=buildItem(item.getGoods(),item.getQuantity(),item.getPrice());
			addItem(ditem);
		}
		changeToApplyStatus();
		fillOrderNo();
		return this;
	}

    /**add bg hf
     * 从特权商城发起的发货
     * @param cart
     * @return
     */
    public DeliveryNote applyFromTmall(ShopCart cart,Logistics logistics){
        applyOrigin=DeliveryApplyOrigin.TMALL;//特权商城订单
        Map<Integer,CartItem> items=cart.getItems();
        Iterator<Integer> ite=items.keySet().iterator();
        Double fee =0.0;
        while(ite.hasNext()){
            CartItem item=items.get(ite.next());
            DeliveryItem ditem=buildItem(item.getGoods(),item.getQuantity(),item.getPrice());
            addItem(ditem);
            fee+=getLogisticsFee(item.getGoods(),item.getQuantity(),logistics);
        }
        setLogisticsFee(fee);//获取物流费
        changeToApplyStatus();
        fillOrderNo();
        return this;
    }

	private void changeToApplyStatus(){
		status=DeliveryStatus.NEW;
		applyTime=new Date();
		printStatus=false;
	}

	private void fillOrderNo(){
		orderNo=DateUtil.date2string(Date.from(Instant.now()), DateUtil.FULL_FORMAT);
	}
	/**
	 * 发货确认
	 */
	public void confirm(){
		if(isConfirm()){
			throw new ApplicationException("不能重复确认发货请求");
		}
		deliveryTime=new Date();
		status=DeliveryStatus.CONFIRM;
		/*Agent agent=userByApplyAgent;
		Double points=caculatePoints();
		if(userByConsignee!=null){
			Agent consignee=(Agent)userByConsignee;
			consignee.increasePoints(points);
		}
		agent.deductPoints(points);*/
		deductBalance();
	}
	/**
	 * 发货
	 */
	public void delivery(){
		if(this.isConfirm()){
			status=DeliveryStatus.DELIVERY;
			deliveryTime=new Date();
			deductStock();
		}
	}

	private void deductStock(){
		deliveryItems.stream().forEach(item->{
			Goods goods=item.getGoods();
			goods.deductCurrentStock(item.getQuantity());
		});
	}

	/**
	 * 计算本次发货产生的积分
	 * @return
	 */
	public Double caculatePoints(){
		return deliveryItems.stream().mapToDouble(item->{return item.caculatePoints();}).sum();
	}

	public Integer getQuantity(){
		return deliveryItems.stream().mapToInt(item->{return item.getQuantity();}).sum();
	}

	/**
	 * 减少本次发货货物余额
	 */
	public void deductBalance(){
		Agent agent=userByApplyAgent;
		Iterator<DeliveryItem> its=deliveryItems.iterator();
		while(its.hasNext()){
			DeliveryItem item=its.next();
			Goods goods=item.getGoods();
			GoodsAccount gac=agent.loadAccountForGoodsNotNull(goods);
			gac.deductBalance(item.getQuantity());
		}
	}


	public DeliveryItem buildItem(Goods goods,Integer quantity){
		GoodsAccount gac=userByApplyAgent.loadAccountForGoodsNotNull(goods);
		if(Objects.isNull(gac)){
			gac=userByApplyAgent.addGoodsAccount(goods);
		}

		DeliveryItem item=new DeliveryItem(goods,quantity);
		return item;
	}

	public DeliveryItem buildItem(Goods goods,Integer quantity,Double price){
		GoodsAccount gac=userByApplyAgent.loadAccountForGoodsNotNull(goods);
		if(Objects.isNull(gac)){
			gac=userByApplyAgent.addGoodsAccount(goods);
		}

		DeliveryItem item=new DeliveryItem(goods,quantity,price);
		return item;
	}

	public boolean agentBalanceNotEnough(GoodsAccount gac,Integer quantity){
		return gac.currentBalanceNotEnough(quantity);
	}

	/**
	 * 返回重量
	 * @param goods
	 * @param quantitys
     */
	public Double addItems(List<Goods> goods,Integer[]quantitys,Logistics logistics){
		int size=goods.size();
		Double fee = 0.0;
		for(int index=0;index<size;index++){
			Goods g=goods.get(index);
			DeliveryItem item=buildItem(g,quantitys[index]);
			addItem(item);
            fee+=getLogisticsFee(g,quantitys[index],logistics);
		}
		return fee;
	}

	public void addItem(DeliveryItem item){
		if(!item.stockEnough() && isSystemApply()){
			throw new ApplicationException(item.getGoods().getName()+" 当前库存不足");
		}

		//商城发货订单，判断上级余额
		if(this.isMallApply()){
//			Agent parent=this.userByApplyAgent.getParent();
//			GoodsAccount parentGac=parent.loadAccountForGoodsNotNull(item.getGoods());
//			if(parentGac.currentBalanceNotEnough(item.getQuantity())){
//				throw new ApplicationException("上级 商品["+item.getGoods().getName()+"] 当前账户余额不足,请提醒上级补货!");
//			}
		}

		GoodsAccount gac=this.userByApplyAgent.loadAccountForGoodsNotNull(item.getGoods());
		//内部系统申请发货,判断申请人余额是否足够
		if(gac.currentBalanceNotEnough(item.getQuantity()) && this.isSystemApply()){
			throw new ApplicationException(userByApplyAgent.getRealName()+"["+item.getGoods().getName()+"] 当前账户余额不足");
		}
		if(deliveryItems.add(item)){
			item.setDeliveryNote(this);
		}
	}

	public void removeItem(DeliveryItem item){
		if(deliveryItems.remove(item)){
			item.setDeliveryNote(null);
		}
	}

	public void cleanItems(){
		deliveryItems.clear();
	}

	public boolean isNew(){
		return Objects.isNull(status)||Objects.equals(status, DeliveryStatus.NEW);
	}

	public boolean isConfirm(){
		return Objects.equals(status, DeliveryStatus.CONFIRM);
	}

	public boolean isDelivery(){
		return Objects.equals(status, DeliveryStatus.DELIVERY);
	}

	public boolean isDisagree(){
		return Objects.equals(status, DeliveryStatus.DISAGREE);
	}
	// Constructors

	/** default constructor */
	public DeliveryNote() {
	}

	/** minimal constructor */
	public DeliveryNote(User userByConsignee, Agent userByApplyAgent,
			String mobile, String address) {
		this.userByConsignee = userByConsignee;
		this.userByApplyAgent = userByApplyAgent;
		this.mobile = mobile;
		this.address = address;
	}

	/** full constructor */
	public DeliveryNote(User userByConsignee, User userByOperator,
			Agent userByApplyAgent, String mobile, String address,
			String remark, DeliveryStatus status, String logisticsCode,
			Double logisticsFee, String logistics, Boolean printStatus,
			Timestamp updateTime, Timestamp applyTime, Timestamp deliveryTime,
			Set<DeliveryItem> deliveryItems) {
		this.userByConsignee = userByConsignee;
		this.userByOperator = userByOperator;
		this.userByApplyAgent = userByApplyAgent;
		this.mobile = mobile;
		this.address = address;
		this.remark = remark;
		this.status = status;
		this.logisticsCode = logisticsCode;
		this.logisticsFee = logisticsFee;
		this.logistics = logistics;
		this.printStatus = printStatus;
		this.updateTime = updateTime;
		this.applyTime = applyTime;
		this.deliveryTime = deliveryTime;
		this.deliveryItems = deliveryItems;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public User getUserByConsignee() {
		return this.userByConsignee;
	}

	public void setUserByConsignee(User userByConsignee) {
		this.userByConsignee = userByConsignee;
	}

	public User getUserByOperator() {
		return this.userByOperator;
	}

	public void setUserByOperator(User userByOperator) {
		this.userByOperator = userByOperator;
	}

	public Agent getUserByApplyAgent() {
		return this.userByApplyAgent;
	}

	public void setUserByApplyAgent(Agent userByApplyAgent) {
		this.userByApplyAgent = userByApplyAgent;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public DeliveryStatus getStatus() {
		return this.status;
	}

	public void setStatus(DeliveryStatus status) {
		this.status = status;
	}

	public String getLogisticsCode() {
		return this.logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public Double getLogisticsFee() {
		return this.logisticsFee;
	}

	public void setLogisticsFee(Double logisticsFee) {
		this.logisticsFee = logisticsFee;
	}

	public String getLogistics() {
		return this.logistics;
	}

	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}

	public Boolean getPrintStatus() {
		return this.printStatus;
	}

	public void setPrintStatus(Boolean printStatus) {
		this.printStatus = printStatus;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Date getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getDeliveryTime() {
		return this.deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Set<DeliveryItem> getDeliveryItems() {
		return this.deliveryItems;
	}

	public void setDeliveryItems(Set<DeliveryItem> deliveryItems) {
		this.deliveryItems = deliveryItems;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public User getParentByOperator() {
        return parentByOperator;
    }

    public void setParentByOperator(User parentByOperator) {
        this.parentByOperator = parentByOperator;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public DeliveryApplyOrigin getApplyOrigin() {
		return applyOrigin;
	}
	public void setApplyOrigin(DeliveryApplyOrigin applyOrigin) {
		this.applyOrigin = applyOrigin;
	}
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public int hashCode(){
		return Objects.hash(id,orderNo);
	}

	@Override
	public boolean equals(Object object){
		if (this == object) {
			return true;
		}
		if (!(object instanceof DeliveryNote)) {
			return false;
		}
		DeliveryNote another=(DeliveryNote)object;
		return Objects.equals(id, another.getId()) && Objects.equals(orderNo, another.getOrderNo());
	}

}