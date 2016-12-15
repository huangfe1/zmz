package com.dreamer.domain.goods;

public enum DeliveryApplyOrigin {
	SYSTEM("请求发货"),MALL("代理商城"),GMALL("官方商城"),TMALL("特权商城");

	private String desc;
	 DeliveryApplyOrigin(String desc){
		this.desc=desc;
	}

}
