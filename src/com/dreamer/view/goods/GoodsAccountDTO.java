package com.dreamer.view.goods;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.AgentStatus;

public class GoodsAccountDTO {
	
	private Goods goods;
	
	private AgentStatus status;
	
	private Double currentPoint;
	private Integer currentBalance;
	private Integer cumulative;
	private Double price;
	
	private Integer maxTradingLimited;
	private Long tradingCumulative;
	
	private String agentName;
	
	private String agentCode;
	
	private String levelName;
	
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public Double getCurrentPoint() {
		return currentPoint;
	}
	public void setCurrentPoint(Double currentPoint) {
		this.currentPoint = currentPoint;
	}
	public Integer getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(Integer currentBalance) {
		this.currentBalance = currentBalance;
	}
	public Integer getCumulative() {
		return cumulative;
	}
	public void setCumulative(Integer cumulative) {
		this.cumulative = cumulative;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public AgentStatus getStatus() {
		return status;
	}
	public void setStatus(AgentStatus status) {
		this.status = status;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public Integer getMaxTradingLimited() {
		return maxTradingLimited;
	}
	public void setMaxTradingLimited(Integer maxTradingLimited) {
		this.maxTradingLimited = maxTradingLimited;
	}
	public Long getTradingCumulative() {
		return tradingCumulative;
	}
	public void setTradingCumulative(Long tradingCumulative) {
		this.tradingCumulative = tradingCumulative;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
}