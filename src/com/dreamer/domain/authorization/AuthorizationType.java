package com.dreamer.domain.authorization;

import java.util.Objects;

import com.dreamer.domain.goods.Goods;

public class AuthorizationType implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3647918139175133853L;
	private Integer id;
	private String name;
	private String remark;
	private Integer order;
	private Integer version;
	private Goods goods;

	public boolean isMainAuthorization() {
		return goods == null ? false : goods.isMainGoods();
	}

	// Constructors

	/** default constructor */
	public AuthorizationType() {
	}

	/** minimal constructor */
	public AuthorizationType(String name) {
		this.name = name;
	}

	/** full constructor */
	public AuthorizationType(String name, String remark, Integer order,
			Integer version) {
		this.name = name;
		this.remark = remark;
		this.order = order;
		this.version = version;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrder() {
		return this.order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		if (goods != null) {
			goods.setAuthorizationType(this);
		}
		this.goods = goods;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id,goods);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof AuthorizationType)) {
			return false;
		}
		AuthorizationType other = (AuthorizationType) object;
		if (Objects.equals(id, other.getId())
				&& Objects.equals(goods, other.getGoods())) {
			return true;
		} else {
			return false;
		}
	}

}