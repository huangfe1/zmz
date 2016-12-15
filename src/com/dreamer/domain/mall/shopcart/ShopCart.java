package com.dreamer.domain.mall.shopcart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.dreamer.domain.goods.Goods;

public class ShopCart {
	
	private Map<Integer,CartItem> items=new HashMap<Integer,CartItem>();
	
	public CartItem getItem(Integer goodsId){
		return items.get(goodsId);
	}

	public Integer getQuantity(){
		AtomicInteger quantity=new AtomicInteger(0);
		items.forEach((key,item)->{
			quantity.addAndGet(item.getQuantity());
		});
		return quantity.get();
	}
	
	public Double getAmount(){
		Iterator<Integer> keyIte=items.keySet().iterator();
		Double amount=0.0D;
		while(keyIte.hasNext()){
			CartItem item=items.get(keyIte.next());
			amount+=item.getAmount();
		}
		return amount;
	}
	
	public CartItem addGoods(Goods goods,Integer quantity,Double price){
		CartItem item=items.get(goods.getId());
		if(Objects.nonNull(item)){
			item.increaseQuantity(quantity);
			return item;
		}else{
			item=new CartItem(goods,quantity,price);
			items.put(goods.getId(), item);
			return item;
		}
	}
	
	public void removeGoods(Goods goods,Integer quantity){
		CartItem item=items.get(goods.getId());
		if(Objects.nonNull(item)){
			item.decreaseQuantity(quantity);
			if(item.getQuantity()<=0){
				items.remove(goods.getId());
			}
		}
	}
	
	public void removeItems(Goods goods){
		CartItem item=items.get(goods.getId());
		if(Objects.nonNull(item)){
			items.remove(goods.getId());
		}
	}

	public Map<Integer,CartItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer,CartItem> items) {
		this.items = items;
	}

}
