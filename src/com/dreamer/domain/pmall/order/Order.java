package com.dreamer.domain.pmall.order;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.BalanceNotEnoughException;
import ps.mx.otter.utils.date.DateUtil;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;

public class Order implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer version;
	private Agent user;
	private String orderNo;
	private Timestamp orderTime;
	private OrderStatus status;
	private String consignee;
	private String shippingAddress;
	private String postCode;
	private String mobile;
	private Double discountAmount;
	private String logisticsCode;
	private Double logisticsFee;
	private String logistics;
	private String invoiceTitle;
	private String invoiceItem;
	private Timestamp updateTime;
	private String remark;
	private Double voucher;
	private Double benefitPoints;
	private PaymentWay paymentWay;
	private AcknowledgmentWay acknowledgmentWay;
	private Date paymentTime;
	private PaymentStatus paymentStatus;
	private Date shippingTime;
	private String shippingOperator;
	private String revokeReason;
	private String revokeOperator;
	private Date receiveTime;
	private String refundReason;
	private String refundOperator;
	private Date refundTime;
	
	private Map<Integer, OrderItem> items = new HashMap<Integer, OrderItem>();

	public boolean paymentRequired() {
		return isUnpaid();
	}
	
	public boolean requiredPaymentBeforeShipping(){
		return isUnpaid() && this.paymentWay!=PaymentWay.COD;
	}
	
	public void commit(Agent agent){
		//不需要积分
//		if(!agent.getAccounts().pointsBalanceEnough(getTotalPoints())){
//			throw new BalanceNotEnoughException("您的账户积分不足");
//		}
		paymentStatus=PaymentStatus.UNPAID;
		status=OrderStatus.NEW;
		user=agent;
		orderTime=Timestamp.from(Instant.now());
		orderNo=fillOrderNo();
		voucher=caculateVoucher();
		benefitPoints=caculateBenefitPoints().doubleValue();
	}
	
	public boolean isNew(){
		return Objects.equals(status, OrderStatus.NEW);
	}

	/**
	 * 订单未支付
	 * 
	 * @return
	 */
	public boolean isUnpaid() {
		return Objects.equals(paymentStatus, PaymentStatus.UNPAID);
	}
	
	public boolean isRefund(){
		return Objects.equals(paymentStatus, PaymentStatus.REFUND);
	}

	/**
	 * 订单已支付
	 * 
	 * @return
	 */
	public boolean isPaid() {
		return Objects.equals(paymentStatus, PaymentStatus.PAID);
	}

	/**
	 * 订单已发货
	 * 
	 * @return
	 */
	public boolean isShipped() {
		return Objects.equals(status, OrderStatus.SHIPPED);
	}

	/**
	 * 订单已确认收货
	 * 
	 * @return
	 */
	public boolean isReceived() {
		return Objects.equals(status, OrderStatus.RECEIVED);
	}

	/**
	 * 订单已完成
	 * 
	 * @return
	 */
	public boolean isCompleted() {
		return isReceived();
	}

	/**
	 * 订单已撤销
	 * 
	 * @return
	 */
	public boolean isRevoked() {
		return Objects.equals(status, OrderStatus.REVOKED);
	}
	
	public boolean isReturned() {
		return Objects.equals(status, OrderStatus.RETURNED);
	}
	
	public void pay(PaymentWay paymentWay,Double money){
		this.paymentWay=paymentWay;
		paymentStatus=PaymentStatus.PAID;
		paymentTime=new Date();
        //去掉积分购买
//		user.getAccounts().deductPoints(getTotalPoints());
	}
	
	public void shipping(){
		if(isReceived()){
			throw new ApplicationException("此订单已收货");
		}
		if(isShipped()){
			throw new ApplicationException("此订单已发货");
		}
		if(requiredPaymentBeforeShipping()){
			throw new ApplicationException("非货到付款订单,必须先完成付款后才能发货");
		}
		this.status=OrderStatus.SHIPPED;
		this.shippingTime=new Date();
	}
	
	public void revoke(){
		if(this.isPaid()){
			throw new ApplicationException("已支付订单无法撤销");
		}
		if(this.isRevoked()){
			throw new ApplicationException("已撤销订单无需反复撤销");
		}
		if(!this.isNew()){
			throw new ApplicationException("仅能撤销未支付未发货未退款的新订单");
		}
		status=OrderStatus.REVOKED;
	}
	
	
	public void returnGoods(){
		if(this.isReturned()){
			throw new ApplicationException("已退货订单无需再次操作");
		}
		if(!this.isReceived()){
			throw new ApplicationException("订单必须确认收货后才能退货");
		}
		if(this.isNew()){
			throw new ApplicationException("未发货订单应当执行撤销操作");
		}
		this.status=OrderStatus.RETURNED;
	}
	
	public void refund(){
		if(!this.isReturned() && !this.isRevoked()){
			throw new ApplicationException("只能退款已撤销或已退货订单");
		}
		if(!this.isPaid()){
			throw new ApplicationException("只能退款已付款订单");
		}
		if(this.isRefund()){
			throw new ApplicationException("该订单已退款");
		}
		this.paymentStatus=PaymentStatus.REFUND;
		this.refundTime=new Date();
	}
	
	public void receive(User user){
		if(!this.isShipped()){
			throw new ApplicationException("不是已发货订单,无法进行收货确认");
		}
		if(user.isAdmin()){
			this.acknowledgmentWay=AcknowledgmentWay.Manager;
		}else{
			this.acknowledgmentWay=AcknowledgmentWay.Consignee;
		}
		status=OrderStatus.RECEIVED;
		receiveTime=new Date();
	}

	public void addItem(OrderItem item) {
		if(Objects.nonNull(items.put(item.getGoodsId(), item))){
			item.setOrder(this);
		}
	}

	public void removeItem(OrderItem item) {
		items.remove(item.getGoodsId());
	}

	public Double caculateVoucher() {
		return items.values().stream().mapToDouble(item -> item.getVoucher())
				.sum();
	}

	public Double caculateBenefitPoints() {
		return items.values().stream()
				.mapToDouble(item -> item.getBenefitPoints()).sum();
	}

	public void clearItems() {
		items.clear();
	}

	/**
	 * 订单货物总数量
	 * 
	 * @return
	 */
	public Integer getQuantity() {
		return items.values().stream().mapToInt(item -> item.getQuantity())
				.sum();
	}

	/**
	 * 订单总现金价金额
	 * 
	 * @return
	 */
	public Double getTotalMoney() {
		return items.values().stream()
				.mapToDouble(item -> item.getAmountMoney()).sum();
	}

	/**
	 * 订单总积分价金额
	 * 
	 * @return
	 */
	public Double getTotalPoints() {
		return items.values().stream()
				.mapToDouble(item -> item.getAmountPoints()).sum();
	}

	/**
	 * 订单总价金额
	 * 
	 * @return
	 */
	public Double getTotalPrice() {
		return items.values().stream()
				.mapToDouble(item -> item.getAmountPrice()).sum();
	}

	/**
	 * 应支付金额
	 * 
	 * @return
	 */
	public Double getActualPayment() {
		return getTotalMoney() - this.getDiscountAmount();
	}

	// Constructors

	/** default constructor */
	public Order() {
	}

	/** minimal constructor */
	public Order(String orderNo, Double voucher) {
		this.orderNo = orderNo;
		this.voucher = voucher;
	}

	/** full constructor */
	public Order(Agent user, String orderNo, Timestamp orderTime,
			OrderStatus status, String consignee, String shippingAddress,
			String postCode, String mobile, Double discountAmount,
			String logisticsCode, Double logisticsFee, String logistics,
			String invoiceTitle, String invoiceItem, Timestamp updateTime,
			Double voucher, Double benefitPoints) {
		this.user = user;
		this.orderNo = orderNo;
		this.orderTime = orderTime;
		this.status = status;
		this.consignee = consignee;
		this.shippingAddress = shippingAddress;
		this.postCode = postCode;
		this.mobile = mobile;
		this.discountAmount = discountAmount;
		this.logisticsCode = logisticsCode;
		this.logisticsFee = logisticsFee;
		this.logistics = logistics;
		this.invoiceTitle = invoiceTitle;
		this.invoiceItem = invoiceItem;
		this.updateTime = updateTime;
		this.voucher = voucher;
		this.benefitPoints = benefitPoints;
	}

	public String fillOrderNo(){
		return Integer.toHexString(Objects.hashCode(user.getAgentCode()))+DateUtil.date2string(Date.from(Instant.now()), "MMddHHmmss");
	}
	
	public static void main(String[] args){
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

	public Agent getUser() {
		return this.user;
	}

	public void setUser(Agent user) {
		this.user = user;
	}

	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Timestamp getOrderTime() {
		return this.orderTime;
	}

	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}

	public OrderStatus getStatus() {
		return this.status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getConsignee() {
		return this.consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getShippingAddress() {
		return this.shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Double getDiscountAmount() {
		return this.discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
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

	public String getInvoiceTitle() {
		return this.invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceItem() {
		return this.invoiceItem;
	}

	public void setInvoiceItem(String invoiceItem) {
		this.invoiceItem = invoiceItem;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Double getVoucher() {
		return voucher;
	}

	public void setVoucher(Double voucher) {
		this.voucher = voucher;
	}

	public Double getBenefitPoints() {
		return this.benefitPoints;
	}

	public void setBenefitPoints(Double benefitPoints) {
		this.benefitPoints = benefitPoints;
	}

	public Map<Integer, OrderItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, OrderItem> items) {
		this.items = items;
	}

	public PaymentWay getPaymentWay() {
		return paymentWay;
	}

	public void setPaymentWay(PaymentWay paymentWay) {
		this.paymentWay = paymentWay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public AcknowledgmentWay getAcknowledgmentWay() {
		return acknowledgmentWay;
	}

	public void setAcknowledgmentWay(AcknowledgmentWay acknowledgmentWay) {
		this.acknowledgmentWay = acknowledgmentWay;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Date getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}

	public String getShippingOperator() {
		return shippingOperator;
	}

	public void setShippingOperator(String shippingOperator) {
		this.shippingOperator = shippingOperator;
	}

	public String getRevokeReason() {
		return revokeReason;
	}

	public void setRevokeReason(String revokeReason) {
		this.revokeReason = revokeReason;
	}

	public String getRevokeOperator() {
		return revokeOperator;
	}

	public void setRevokeOperator(String revokeOperator) {
		this.revokeOperator = revokeOperator;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getRefundOperator() {
		return refundOperator;
	}

	public void setRefundOperator(String refundOperator) {
		this.refundOperator = refundOperator;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}
	
}