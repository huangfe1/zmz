package com.dreamer.domain.goods;

import java.util.Date;

import com.dreamer.domain.user.AgentLevel;

public class Price implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6973903151410018364L;
	// Fields

	private Integer id;
	private Goods goods;
	//价格等级
	private AgentLevel agentLevel;
	private Double price;
	private Date updateTime;
	private Integer version;
	private Integer threshold;

	// Constructors

	/** default constructor */
	public Price() {
	}

	/** minimal constructor */
	public Price(Goods goods, AgentLevel agentLevel, Double price) {
		this.goods = goods;
		this.agentLevel = agentLevel;
		this.price = price;
	}

	/** full constructor */
	public Price(Goods goods, AgentLevel agentLevel, Double price,
			Date updateTime, Integer version) {
		this.goods = goods;
		this.agentLevel = agentLevel;
		this.price = price;
		this.updateTime = updateTime;
		this.version = version;
	}
	
	public boolean thresholdLowerThan(Integer cumulative){
		return this.getThreshold()<=cumulative;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Goods getGoods() {
		return this.goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public AgentLevel getAgentLevel() {
		return this.agentLevel;
	}

	public void setAgentLevel(AgentLevel agentLevel) {
		this.agentLevel = agentLevel;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
	

	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode())
				+ ((goods == null) ? 0 : goods.hashCode())
				+ ((agentLevel == null) ? 0 : agentLevel.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Price)) {
			return false;
		}
		Price other = (Price) object;
		if (null == this.id) {
			if (null != other.getId()) {
				return false;
			}
		} else {
			if (other.getId() == this.getId()) {
				return true;
			} else {
				return false;
			}
		}
		if(null==this.goods){
			if(null!=other.getGoods()){
				return false;
			}
		}else{
			if(this.goods.equals(other.getGoods())){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

}