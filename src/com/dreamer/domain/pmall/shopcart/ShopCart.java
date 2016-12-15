package com.dreamer.domain.pmall.shopcart;

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.pmall.order.OrderItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ShopCart {
	
	public List<OrderItem> buildOrderItems(){
		return items.values().stream().map(i->{
			OrderItem item=new OrderItem();
			item.setGoodsName(i.getGoods().getName());
			item.setGoodsId(i.getGoods().getId());
			item.setGoodsSpec(i.getGoods().getSpec());
			item.setMoneyPrice(i.getMoneyPrice());
			item.setPointsPrice(i.getPointsPrice());
			item.setPrice(i.getPrice());
			item.setQuantity(i.getQuantity());
			item.setBenefitPoints(i.getGoods().caculateBenefitPoints(i.getQuantity()));
			item.setVoucher(i.getGoods().caculateVoucher(i.getQuantity()));
			item.setVouchers(i.getGoods().getVouchers());//设置返利模式
			return item;
		}).collect(Collectors.toList());
	}

	private Map<Integer, CartItem> items = new HashMap<Integer, CartItem>();

	public Integer getQuantity() {
		AtomicInteger quantity = new AtomicInteger(0);
		items.forEach((key, item) -> {
			quantity.addAndGet(item.getQuantity());
		});
		return quantity.get();
	}

	public Double getMoneyAmount() {
		Iterator<Integer> keyIte = items.keySet().iterator();
		Double amount = 0.0D;
		while (keyIte.hasNext()) {
			CartItem item = items.get(keyIte.next());
			amount += item.getMoneyAmount();
		}
		return amount;
	}

	public Double getPointsAmount() {
		Iterator<Integer> keyIte = items.keySet().iterator();
		Double amount = 0.0D;
		while (keyIte.hasNext()) {
			CartItem item = items.get(keyIte.next());
			amount += item.getPointsAmount();
		}
		return amount;
	}

	public CartItem addGoods(MallGoods goods, Integer quantity, Double price,
			Double moneyPrice, Double pointsPrice) {
		CartItem item = items.get(goods.getId());
		if (Objects.nonNull(item)) {
			item.increaseQuantity(quantity);
			return item;
		} else {
			item = new CartItem(goods, quantity, price, moneyPrice, pointsPrice);
			items.put(goods.getId(), item);
			return item;
		}
	}

	/**
	 * 获取购物车的数量
	 * @param goods
	 * @return
     */
	public Integer getGoodsQuantitu(MallGoods goods){
		CartItem item = items.get(goods.getId());
		if (Objects.isNull(item)) {
			return 0;
		}
		return item.getQuantity();
	}

	public void removeGoods(MallGoods goods, Integer quantity) {
		CartItem item = items.get(goods.getId());
		if (Objects.nonNull(item)) {
			item.decreaseQuantity(quantity);
			if (item.getQuantity() <= 0) {
				items.remove(goods.getId());
			}
		}
	}

	public void removeItems(MallGoods goods) {
		CartItem item = items.get(goods.getId());
		if (Objects.nonNull(item)) {
			items.remove(goods.getId());
		}
	}

	public Map<Integer, CartItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, CartItem> items) {
		this.items = items;
	}

}
