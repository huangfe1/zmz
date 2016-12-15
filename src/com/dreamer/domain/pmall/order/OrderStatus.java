package com.dreamer.domain.pmall.order;

public enum OrderStatus {

	NEW("新下单"),SHIPPED("已发货"),RECEIVED("已收货"),REVOKED("作废"),RETURNED("已退货");
	
	private String name;
	
	public String getName(){
		return name;
	}
	
	OrderStatus(String name){
		this.name=name;
	}
}
