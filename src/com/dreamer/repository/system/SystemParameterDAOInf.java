package com.dreamer.repository.system;

import java.util.List;
import java.util.function.Function;

import ps.mx.otter.utils.SearchParameter;

import com.dreamer.domain.system.SystemParameter;

public interface SystemParameterDAOInf {

	/**
	 * 获取设置的代理编码前缀
	 * 
	 * @return
	 */
	public abstract String getPrefixOfAgent();
	
	/**
	 * 获取设置的防伪码前缀,此前缀之后的内容为防伪码连续号
	 * @return
	 */
	public abstract String getPrefixOfSecurityCode();
	
	/**
	 * 获取设置的防伪码长度
	 * @return
	 */
	public abstract Integer getSecurityCodeLength();
	
	/**
	 * 获取设置的防伪码分隔符
	 * @return
	 */
	public abstract String getSecurityCodeDelimiter();
	
	public abstract Integer getGiftPoints();
	
	/**
	 * 获取设置的代理编码主体生成的种子值
	 * 
	 * @return
	 */
	public abstract String getSeedOfAgent();

	public abstract void updateSeedOfAgent(String seed);

	/**
	 * 获取设置的代理编码主体部分长度
	 * 
	 * @return 编码主体部分长度 未设置时返回默认长度
	 */
	public abstract int getAgentCodeBodyLen();

	/**
	 * 获取设置的代理编码主体部分填充符,当编码体长度不足设置的编码主体部分长度时,使用本填充符补位
	 * @return 代理编码主体部分填充符，不存在时返回缺省填充符
	 */
	public abstract String getAgentCodePadding();
	
	public abstract String getGoodsImgPath();
	
	public abstract String getMallGoodsImgPath();
	
	public abstract String getAvoidAudit();
	
	public abstract Double getCoupons();
	
	public abstract Integer getCouponsLevel();
	
	public abstract Integer getBenefitPoints();
	
	public abstract Integer getBenefitPointsLevel();
	
	public List<SystemParameter> searchEntityByPage(
			SearchParameter<SystemParameter> p,
			Function<SearchParameter<SystemParameter>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries);

	public abstract void save(SystemParameter transientInstance);

	public abstract void delete(SystemParameter persistentInstance);

	public abstract SystemParameter findById(java.lang.String id);

	public abstract List<SystemParameter> findByExample(SystemParameter instance);

	public abstract List<SystemParameter> findByProperty(String propertyName,
			Object value);

	public abstract List<SystemParameter> findByVersion(Object version);

	public abstract List<SystemParameter> findByParamValue(Object paramValue);

	public abstract List<SystemParameter> findByName(Object name);

	public abstract List<SystemParameter> findAll();

	public abstract SystemParameter merge(SystemParameter detachedInstance);

	public abstract void attachDirty(SystemParameter instance);

	public abstract void attachClean(SystemParameter instance);

}