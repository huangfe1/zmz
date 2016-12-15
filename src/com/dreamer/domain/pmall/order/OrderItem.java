package com.dreamer.domain.pmall.order;


import java.util.Objects;

public class OrderItem {

	private Integer id;


	private Order order;
	
	private Integer goodsId;
	
	private String goodsName;
	
	private String goodsSpec;
	
	private Double benefitPoints;
	
	private Double voucher;

    private String vouchers;
	
	private Integer quantity;
	
	private Double price;
	
	private Double pointsPrice;
	
	private Double moneyPrice;
	
	private Double discountAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmountPoints(){
		return pointsPrice*quantity;
	}
	
	public Double getAmountMoney(){
		return moneyPrice*quantity;
	}
	
	public Double getAmountPrice(){
		return price*quantity;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

    public String getVouchers() {
        return vouchers;
    }

    public void setVouchers(String vouchers) {
        this.vouchers = vouchers;
    }

    public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPointsPrice() {
		return pointsPrice;
	}

	public void setPointsPrice(Double pointsPrice) {
		this.pointsPrice = pointsPrice;
	}

	public Double getMoneyPrice() {
		return moneyPrice;
	}

	public void setMoneyPrice(Double moneyPrice) {
		this.moneyPrice = moneyPrice;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public Double getBenefitPoints() {
		return benefitPoints;
	}

	public void setBenefitPoints(Double benefitPoints) {
		this.benefitPoints = benefitPoints;
	}

	public Double getVoucher() {
		return voucher;
	}

	public void setVoucher(Double voucher) {
		this.voucher = voucher;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(goodsId);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(Objects.isNull(obj)){
			return false;
		}
		if(!(obj instanceof OrderItem)){
			return false;
		}
		OrderItem another=(OrderItem)obj;
		return Objects.equals(goodsId, another.getGoodsId());
		
	}
	
	
}
