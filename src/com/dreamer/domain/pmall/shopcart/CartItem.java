package com.dreamer.domain.pmall.shopcart;

import java.math.BigDecimal;
import java.util.Objects;

import com.dreamer.domain.mall.goods.MallGoods;

public class CartItem {

	private Integer quantity = 0;
	private MallGoods goods;
	private Double price = 0.0D;
	private Double pointsPrice = 0.0D;
	private Double moneyPrice = 0.0D;

	public CartItem(MallGoods goods, Integer quantity, Double price,
			Double moneyPrice, Double pointsPrice) {
		this.goods = goods;
		this.quantity = quantity;
		this.price = price;
		this.moneyPrice = moneyPrice;
		this.pointsPrice = pointsPrice;
	}

	public CartItem() {
	}

	public CartItem increaseQuantity(Integer quantity) {
		setQuantity(getQuantity() + quantity);
		return this;
	}

	public CartItem decreaseQuantity(Integer quantity) {
		setQuantity(getQuantity() - quantity);
		return this;
	}

	public Double getMoneyAmount() {
		BigDecimal p=new BigDecimal(this.getMoneyPrice() * this.getQuantity());
		return p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public Double getPointsAmount() {
		BigDecimal p=new BigDecimal( this.getPointsPrice() * this.getQuantity());
		return p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public MallGoods getGoods() {
		return goods;
	}

	public void setGoods(MallGoods goods) {
		this.goods = goods;
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

	@Override
	public int hashCode() {
		return Objects.hashCode(goods);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof CartItem)) {
			return false;
		}
		CartItem item = (CartItem) object;
		return Objects.equals(goods, item.getGoods());
	}

}
