package com.dreamer.domain.goods;

import com.dreamer.domain.user.Enum;

public enum GoodsTransferStatus implements Enum{

	NEW("新申请"),CONFIRM("已确认"),DISAGREE("不同意");
	
	GoodsTransferStatus(String desc){
		this.desc=desc;
	}	
	public String getDesc(){
		return desc;
	}
	
	private String desc;
}
