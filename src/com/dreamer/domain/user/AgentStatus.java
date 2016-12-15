package com.dreamer.domain.user;

public enum AgentStatus implements Enum{

	ACTIVE("激活"),NO_ACTIVE("未激活"),REORGANIZE("整顿");
	
	AgentStatus(String desc){
		this.desc=desc;
	}	
	public String getDesc(){
		return desc;
	}
	
	private String desc;
}
