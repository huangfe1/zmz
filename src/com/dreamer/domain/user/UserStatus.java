package com.dreamer.domain.user;


public enum UserStatus implements Enum {

	NEW("新申请"),NORMAL("正常"),STOP("停用"),LOCKED("锁定"),DELETE("删除"),TRANSFER("已转让");
	
	UserStatus(String desc){
		this.desc=desc;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return desc;
	}
	
	private String desc;
}
