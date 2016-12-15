package com.dreamer.domain.pmall.order;

public enum PaymentWay {
	COD("货到付款"),WX("微信支付"),ZFB("支付宝"),YL("银联");
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	PaymentWay(String name){
		this.name=name;
	}
}
