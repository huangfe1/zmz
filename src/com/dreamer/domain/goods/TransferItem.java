package com.dreamer.domain.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class TransferItem {
	
	private Transfer transfer;
	
	private Integer quantity;
	
	private Double points;
	
	private Integer goodsId;
	
	private String goodsName;
	
	private Double price;
	
	private String spec;
	
	private Goods goods;
	
	private String priceLevelName;
	
	public static TransferItem build(Goods goods,Integer quantity,Price price){
		TransferItem item=new TransferItem(goods,quantity,price.getPrice(),price.getAgentLevel().getName());
		return item;
	}
	
	
	
	
	public TransferItem(){}
	
	private TransferItem(Goods goods,Integer quantity){
		this.goodsId=goods.getId();
		this.goodsName=goods.getName();
		this.quantity=quantity;
		this.goods=goods;
		points=goods.caculatePoints(quantity);
	}
	
	
	private TransferItem(Goods goods,Integer quantity,Double price,String priceLevelName){
		this.goodsId=goods.getId();
		this.goodsName=goods.getName();
		this.quantity=quantity;
		this.price=price;
		this.goods=goods;
		this.priceLevelName=priceLevelName;
		points=goods.caculatePoints(quantity);
	}
	
	
	public Double getAmount(){
		BigDecimal p=new BigDecimal( price*quantity);
		return p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public Transfer getTransfer() {
		return transfer;
	}

	public void setTransfer(Transfer transfer) {
		this.transfer = transfer;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
	
	public String getPriceLevelName() {
		return priceLevelName;
	}

	public void setPriceLevelName(String priceLevelName) {
		this.priceLevelName = priceLevelName;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(goodsId);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(this==obj){return true;}
		if(! (obj instanceof TransferItem)){
			return false;
		}
		TransferItem another=(TransferItem)obj;
		return Objects.equals(goodsId, another.getGoodsId());
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}
}
