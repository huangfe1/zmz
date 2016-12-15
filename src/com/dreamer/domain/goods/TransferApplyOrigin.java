package com.dreamer.domain.goods;

public enum TransferApplyOrigin {

	MALL("代理商城"), GMALL("官方商城"), TMALL("特权商城"), OUT("主动转出"),DELIVERY("直接发货"), BACK("退给上级");

    private String desc;

	TransferApplyOrigin(String desc){
	this.desc=desc;
	}

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
