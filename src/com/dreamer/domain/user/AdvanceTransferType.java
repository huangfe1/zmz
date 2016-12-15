package com.dreamer.domain.user;

public enum AdvanceTransferType {
	NORMAL("主动转出"),PAY("充值"),ERROR("失败/未付款");
    public String desc;

    AdvanceTransferType(String desc){
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}