package com.dreamer.domain.system;

/**
 * 系统参数
 * 
 * @author Tankman
 *
 */
public class SystemParameter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1871573252911250608L;
	private String code;
	private Integer version;
	private String paramValue;
	private String name;
	private String remark;
	// Constructors

	/** default constructor */
	public SystemParameter() {
	}

	/** minimal constructor */
	public SystemParameter(String code, String paramValue) {
		this.code = code;
		this.paramValue = paramValue;
	}

	/** full constructor */
	public SystemParameter(String code, String paramValue, String name) {
		this.code = code;
		this.paramValue = paramValue;
		this.name = name;
	}

	// Property accessors

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}