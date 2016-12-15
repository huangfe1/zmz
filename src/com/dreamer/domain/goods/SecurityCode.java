package com.dreamer.domain.goods;

import java.util.Date;

public class SecurityCode implements java.io.Serializable {

	private static final long serialVersionUID = 3207983283234294648L;
	private Integer id;
	private Integer version;
	private String code;
	private String owner;
	private String goodsName;
	private Date updateTime;
	private String recorder;
	private String agentCode;

	// Constructors

	/** default constructor */
	public SecurityCode() {
	}

	/** minimal constructor */
	public SecurityCode(String code, String owner, String goodsName) {
		this.code = code;
		this.owner = owner;
		this.goodsName = goodsName;
	}

	/** full constructor */
	public SecurityCode(String code, String owner, String goodsName,
			Date updateTime, String recorder) {
		this.code = code;
		this.owner = owner;
		this.goodsName = goodsName;
		this.updateTime = updateTime;
		this.recorder = recorder;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRecorder() {
		return this.recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	
	

}