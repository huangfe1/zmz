package com.dreamer.domain.mall.shopcart;

import java.math.BigDecimal;
import java.util.Objects;

import com.dreamer.domain.goods.Goods;

public class CartItem {
	
	private Integer quantity=0;
	private Goods goods;
	private Double price=0.0D;
	private String priceLevelName;
	
	public CartItem(Goods goods,Integer quantity,Double price){
		this.goods=goods;
		this.quantity=quantity;
		this.price=price;
	}
	
	public CartItem(){}
	
	public CartItem increaseQuantity(Integer quantity){
		setQuantity(getQuantity()+quantity);
		return this;
	}
	
	public CartItem decreaseQuantity(Integer quantity){
		setQuantity(getQuantity()-quantity);
		return this;
	}
	
	public Double getAmount(){
		BigDecimal p=new BigDecimal( this.getPrice()*this.getQuantity());
		return p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	
	public String getPriceLevelName() {
		return priceLevelName;
	}

	public void setPriceLevelName(String priceLevelName) {
		this.priceLevelName = priceLevelName;
	}

	@Override
	public int hashCode(){
		return Objects.hashCode(goods);
	}
	
	@Override
	public boolean equals(Object object){
		if (this == object) {
			return true;
		}
		if (!(object instanceof CartItem)) {
			return false;
		}
		CartItem item=(CartItem)object;
		return Objects.equals(goods, item.getGoods());
	}

}
