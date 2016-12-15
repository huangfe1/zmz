package com.dreamer.domain.account;

import com.dreamer.domain.user.Agent;

import java.sql.Timestamp;
import java.util.Date;

public class VoucherRecord implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer type;//记录类型
	
	private String more;//备注
	
	private Agent agent;//谁变动
	
	private Double voucher;//代金券
	
	private Date updateTime;//更新时间
	
	private Integer version;//数据版本

	private Double voucher_now;//变更后的代金券

	private Integer hasNoticed;//0需要通知  1已经通知

	public VoucherRecord(){
		
	}
	
	public VoucherRecord(Integer type,Double voucher,String more,Timestamp updateTime,Double voucher_now){
		this.type=type;
		this.voucher=voucher;
		this.more=more;
		this.updateTime=updateTime;
		this.voucher_now=voucher_now;
		this.hasNoticed=0;//需要通知
	}

	public Integer getHasNoticed() {
		return hasNoticed;
	}

	public void setHasNoticed(Integer hasNoticed) {
		this.hasNoticed = hasNoticed;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}



	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public Double getVoucher() {
		return voucher;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setVoucher(Double voucher) {
		this.voucher = voucher;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Double getVoucher_now() {
		return voucher_now;
	}

	public void setVoucher_now(Double voucher_now) {
		this.voucher_now = voucher_now;
	}
	
	
	
	
}
