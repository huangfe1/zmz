package com.dreamer.domain.user;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.dreamer.domain.goods.GoodsType;

/**
 * 产品价格等级
 * @author Tankman
 *
 */
public class AgentLevel implements java.io.Serializable {

	private static final long serialVersionUID = 4978274803686773936L;
	private Integer id;
	private String name;
	private GoodsType goodsType;
	private AgentLevel parent;
	private Set<AgentLevel> children=new HashSet<AgentLevel>();
	private Integer level;
	private Integer order;
	private Timestamp updateTime;
	private Integer version;
	private Boolean auto_promotion;//是否能自动晋升到本等级
	
	public void addChild(AgentLevel agentLevel){
		children.add(agentLevel);
	}
	/**
	 * 等级比我低
	 * @param agentLevel 参与比较的级别,null或者级别低于本级别时返回true
	 * @return
	 */
	public boolean lowerThanMe(AgentLevel agentLevel){
		return agentLevel==null || level<agentLevel.getLevel();
	}
	/**
	 * 等级比我高
	 * @param agentLevel
	 * @return
	 */
	public boolean higherThanMe(AgentLevel agentLevel){
		return Objects.nonNull(agentLevel)
				&& level>agentLevel.getLevel();
	}
	/**
	 * 相同等级
	 * @param agentLevel
	 * @return
	 */
	public boolean myEqual(AgentLevel agentLevel){
		return Objects.equals(level, agentLevel.getLevel());
	}
	
	public boolean canAutoPromotion(){
		return auto_promotion;
	}


	/** default constructor */
	public AgentLevel() {
	}

	/** minimal constructor */
	public AgentLevel(String name) {
		this.name = name;
	}

	/** full constructor */
	public AgentLevel(String name, AgentLevel parent, Integer level, Integer order, Timestamp updateTime,
			Integer version) {
		this.name = name;
		this.parent = parent;
		this.level = level;
		this.order = order;
		this.updateTime = updateTime;
		this.version = version;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public GoodsType getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(GoodsType goodsType) {
		this.goodsType = goodsType;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AgentLevel getParent() {
		return this.parent;
	}

	public void setParent(AgentLevel parent) {
		this.parent = parent;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Set<AgentLevel> getChildren() {
		return children;
	}

	public void setChildren(Set<AgentLevel> children) {
		this.children = children;
	}
	
	public Boolean getAuto_promotion() {
		return auto_promotion;
	}
	
	
	
	public void setAuto_promotion(Boolean auto_promotion) {
		this.auto_promotion = auto_promotion;
	}
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id)
				+ ((name == null) ? 0 : name.hashCode())
				+((level == null) ? 0 : level.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof AgentLevel)) {
			return false;
		}
		AgentLevel other = (AgentLevel) object;
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
		return true;
	}
}