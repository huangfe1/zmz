package com.dreamer.domain.user;

import java.util.Date;

public class PointsTransfer implements java.io.Serializable {

	private static final long serialVersionUID = 2759558931721988426L;
	// Fields

	private Integer id;
	private Integer version;
	private User userByToAgent;
	private User userByFromAgent;
	private Date transferTime;
	private Date updateTime;
	private String remark;
	private Double point;

	// Constructors
	
	
	public void transferPoints(Agent from,Agent to,Double points){
		userByToAgent=to;
		userByFromAgent=from;
		transferTime=new Date();
		point=points;
		from.transferPoints(to, points);
	}

	/** default constructor */
	public PointsTransfer() {
	}

	/** minimal constructor */
	public PointsTransfer(User userByToAgent, User userByFromAgent, Double point) {
		this.userByToAgent = userByToAgent;
		this.userByFromAgent = userByFromAgent;
		this.point = point;
	}

	/** full constructor */
	public PointsTransfer(User userByToAgent, User userByFromAgent,
			Date transferTime, Date updateTime, String remark,
			Double point) {
		this.userByToAgent = userByToAgent;
		this.userByFromAgent = userByFromAgent;
		this.transferTime = transferTime;
		this.updateTime = updateTime;
		this.remark = remark;
		this.point = point;
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

	public User getUserByToAgent() {
		return this.userByToAgent;
	}

	public void setUserByToAgent(User userByToAgent) {
		this.userByToAgent = userByToAgent;
	}

	public User getUserByFromAgent() {
		return this.userByFromAgent;
	}

	public void setUserByFromAgent(User userByFromAgent) {
		this.userByFromAgent = userByFromAgent;
	}

	public Date getTransferTime() {
		return this.transferTime;
	}

	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getPoint() {
		return this.point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

}