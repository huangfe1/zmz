package com.dreamer.domain.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Module implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2455946669635791065L;
	// Fields

	private Integer id;
	private Integer version;
	private String name;
	private String url;
	private Boolean leaf;
	private Module parent;
	private Integer sequence;
	private Timestamp updateTime;
	private String cssClass;
	private List<Module> children = new ArrayList<Module>();

	// Constructors

	/** default constructor */
	public Module() {
	}

	public boolean isTop() {
		return Objects.isNull(parent);
	}

	/** minimal constructor */
	public Module(String name) {
		this.name = name;
	}

	/** full constructor */
	public Module(String name, String url, Boolean leaf, Module parent,
			Integer sequence, Timestamp updateTime) {
		this.name = name;
		this.url = url;
		this.leaf = leaf;
		this.parent = parent;
		this.sequence = sequence;
		this.updateTime = updateTime;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getLeaf() {
		return this.leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Module getParent() {
		return this.parent;
	}

	public void setParent(Module parent) {
		this.parent = parent;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public List<Module> getChildren() {
		Collections.sort(children, new ModuleComparator());
		return children;
	}

	public void setChildren(List<Module> children) {
		this.children = children;
	}
	
	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(id, name, url, sequence);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Module)) {
			return false;
		}
		Module another = (Module) obj;
		return Objects.equals(id, another.getId())
				&& Objects.equals(name, another.getName())
				&& Objects.equals(url, another.getUrl())
				&& Objects.equals(sequence, another.getSequence());
	}
}