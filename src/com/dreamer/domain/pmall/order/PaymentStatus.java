package com.dreamer.domain.pmall.order;

public enum PaymentStatus {
	UNPAID("未支付"), PAID("已支付"),REFUND("已退款");

	private String name;

	public String getName() {
		return name;
	}

	PaymentStatus(String name) {
		this.name = name;
	}
}
