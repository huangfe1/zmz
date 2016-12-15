package com.dreamer.domain.pmall.order;

/**
 * 订单确认收货方式
 * @author miaoxin
 *
 */
public enum AcknowledgmentWay {
	Consignee("收货人确认"),Manager("管理者确认"),Expressman("快递员确认");
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	AcknowledgmentWay(String name){
		this.name=name;
	}
}
