package com.dreamer.repository.system;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.system.SystemParameter;

@Repository("systemParameterDAO")
@Transactional
public class SystemParameterDAO extends HibernateBaseDAO<SystemParameter> implements SystemParameterDAOInf {
	private static final Logger log = LoggerFactory
			.getLogger(SystemParameterDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String PARAM_VALUE = "paramValue";
	public static final String NAME = "name";
	public static final String PREFIX_OF_AGENT_CODE = "01";
	public static final String SEED_OF_AGENT_CODE = "02";//代理编码体种子值系统参数代码
	public static final String AVOID_AUDIT="03";//代理是否免审核
	public static final String AGENTCODE_BODY_LEN = "04";//代理编码体长度系统参数代码
	public static final String AGENTCODE_PADDING="05";//代理编码占位符系统参数代码
	public static final String PREFIX_OF_SECURITY_CODE="06";
	public static final String SECURITY_CODE_LEN="07";
	public static final String SECURITY_CODE_DELIMITER="08";
	public static final String DEFAULT_SECURITY_CODE_DELIMITER=",";
	public static final int DEFAULT_AGENTCODE_BODY_LEN = 6;
	public static final int DEFAULT_SECURITY_CODE_LEN=8;
	public static final String DEFAULT_PREFIX_OF_SECURITY_CODE="ZMZ";
	public static final String DEFAULT_AGENTCODE_PADDING = "0";
	public static final String DEFAULT_PREFIX_OF_AGENT_CODE = "ZMZ";
	public static final String GOODS_IMG_PATH="09";
	public static final String DEFAULT_GOODS_IMG_PATH = "/dreamer/goods";
	public static final String MALLGOODS_IMG_PATH="10";
	public static final String DEFAULT_MALLGOODS_IMG_PATH="/dreamer/mallgoods";
	public static final String GIFTPOINT="11";
	public static final Integer DEFAULT_GIFTPOINTS=0;
	public static final String COUPONS="12";
	public static final String COUPONS_LEVEL="14";
	public static final String BENEFIT_POINTS="13";
	public static final String BENEFIT_POINTS_LEVEL="15";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#getPrefixOfAgent()
	 */
	@Override
	public String getPrefixOfAgent() {
		SystemParameter param = this.findById(PREFIX_OF_AGENT_CODE);
		if (param != null) {
			return param.getParamValue();
		} else {
			return DEFAULT_PREFIX_OF_AGENT_CODE;
		}
	}
	
	

	@Override
	public String getPrefixOfSecurityCode() {
		// TODO Auto-generated method stub
		SystemParameter param = this.findById(PREFIX_OF_SECURITY_CODE);
		if (param != null) {
			if(param.getParamValue().trim().equals("-1")){
				return "";
			}
			return param.getParamValue();
		} else {
			return DEFAULT_PREFIX_OF_SECURITY_CODE;
		}
	}
	
	
	
	@Override
	public String getSecurityCodeDelimiter() {
		// TODO Auto-generated method stub
		SystemParameter param = this.findById(SECURITY_CODE_DELIMITER);
		if (param != null) {
			return param.getParamValue();
		} else {
			return DEFAULT_SECURITY_CODE_DELIMITER;
		}
	}

	@Override
	public Integer getSecurityCodeLength() {
		// TODO Auto-generated method stub
		SystemParameter param = this.findById(SECURITY_CODE_LEN);
		if (param != null) {
			
			try{
				int len= Integer.parseInt(param.getParamValue());
				return len;
			}catch(Exception exp){
				exp.printStackTrace();
				return DEFAULT_SECURITY_CODE_LEN;
			}
			
		} else {
			return DEFAULT_SECURITY_CODE_LEN;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#getSeedOfAgent()
	 */
	@Override
	public String getSeedOfAgent() {
		SystemParameter param = this.findById(SEED_OF_AGENT_CODE);
		try {
			if (param != null) {
				return param.getParamValue();
			} else {
				return null;
			}
		} catch (Exception exp) {
			return "0";
		}
	}
	
	
	
	@Override
	public String getAvoidAudit() {
		// TODO Auto-generated method stub
		SystemParameter param = this.findById(AVOID_AUDIT);
		try {
			if (param != null) {
				return param.getParamValue();
			} else {
				return "0";
			}
		} catch (Exception exp) {
			return "0";
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#updateSeedOfAgent(java.lang.String)
	 */
	@Override
	public void updateSeedOfAgent(String seed) {
		SystemParameter param = this.findById(SEED_OF_AGENT_CODE);
		param.setParamValue(seed);
		this.merge(param);
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#getAgentCodeBodyLen()
	 */
	@Override
	public int getAgentCodeBodyLen() {
		SystemParameter param = this.findById(AGENTCODE_BODY_LEN);
		try {
			if (param != null) {
				return Integer.parseInt(param.getParamValue());
			} else {
				return DEFAULT_AGENTCODE_BODY_LEN;
			}
		} catch (Exception exp) {
			return DEFAULT_AGENTCODE_BODY_LEN;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#getAgentCodePadding()
	 */
	@Override
	public String getAgentCodePadding(){
		SystemParameter param = this.findById(AGENTCODE_PADDING);
		try {
			if (param != null) {
				return param.getParamValue();
			} else {
				return DEFAULT_AGENTCODE_PADDING;
			}
		} catch (Exception exp) {
			return DEFAULT_AGENTCODE_PADDING;
		}
	}
	
	

	@Override
	public String getGoodsImgPath() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(GOODS_IMG_PATH);
		if(param!=null){
			return param.getParamValue();
		}else{
			return DEFAULT_GOODS_IMG_PATH;
		}
	}

	@Override
	public String getMallGoodsImgPath() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(MALLGOODS_IMG_PATH);
		if(param!=null){
			return param.getParamValue();
		}else{
			return DEFAULT_MALLGOODS_IMG_PATH;
		}
	}
	
	

	@Override
	public Integer getGiftPoints() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(GIFTPOINT);
		try{
			if(Objects.nonNull(param)){
				return Integer.parseInt(param.getParamValue());
			}else{
				return DEFAULT_GIFTPOINTS;
			}
		}catch(NumberFormatException exp){
			exp.printStackTrace();
			return DEFAULT_GIFTPOINTS;
		}
	}
	
	

	/**
	 * 积分商城返券
	 */
	@Override
	public Double getCoupons() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(COUPONS);
		Double defaultCoupons=0D;
		try{
			if(Objects.nonNull(param)){
				return Double.parseDouble(param.getParamValue());
			}else{
				return defaultCoupons;
			}
		}catch(NumberFormatException exp){
			exp.printStackTrace();
			return defaultCoupons;
		}
	}

	/**
	 * 返券层级
	 */
	@Override
	public Integer getCouponsLevel() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(COUPONS_LEVEL);
		Integer defaultCouponsLevel=1;
		try{
			if(Objects.nonNull(param)){
				return Integer.parseInt(param.getParamValue());
			}else{
				return defaultCouponsLevel;
			}
		}catch(NumberFormatException exp){
			exp.printStackTrace();
			return defaultCouponsLevel;
		}
	}

	/**
	 * 返还福利积分
	 */
	@Override
	public Integer getBenefitPoints() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(BENEFIT_POINTS);
		Integer defaultBenefitPoints=0;
		try{
			if(Objects.nonNull(param)){
				return Integer.parseInt(param.getParamValue());
			}else{
				return defaultBenefitPoints;
			}
		}catch(NumberFormatException exp){
			exp.printStackTrace();
			return defaultBenefitPoints;
		}
	}

	/**
	 * 福利积分返还层级
	 */
	@Override
	public Integer getBenefitPointsLevel() {
		// TODO Auto-generated method stub
		SystemParameter param=this.findById(BENEFIT_POINTS_LEVEL);
		Integer defaultBenefitPointsLevel=1;
		try{
			if(Objects.nonNull(param)){
				return Integer.parseInt(param.getParamValue());
			}else{
				return defaultBenefitPointsLevel;
			}
		}catch(NumberFormatException exp){
			exp.printStackTrace();
			return defaultBenefitPointsLevel;
		}
	}

	@Override
	public List<SystemParameter> searchEntityByPage(
			SearchParameter<SystemParameter> p,
			Function<SearchParameter<SystemParameter>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.ANYWHERE);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example).addOrder(Order.desc("version"));
					return criteria;
				}, getCacheQueries);
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#save(com.dreamer.domain.system.SystemParameter)
	 */
	@Override
	@CachePut(value="simpleCache",key="#transientInstance.code")
	public void save(SystemParameter transientInstance) {
		log.debug("saving SystemParameter instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#delete(com.dreamer.domain.system.SystemParameter)
	 */
	@Override
	@CacheEvict(value="simpleCache",key="#persistentInstance.code")
	public void delete(SystemParameter persistentInstance) {
		log.debug("deleting SystemParameter instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findById(java.lang.String)
	 */
	@Override
	@Cacheable(value="simpleCache",key="#code")
	public SystemParameter findById(java.lang.String code) {
		log.debug("getting SystemParameter instance with id: " + code);
		try {
			SystemParameter instance = (SystemParameter) getCurrentSession()
					.get("com.dreamer.domain.system.SystemParameter", code);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findByExample(com.dreamer.domain.system.SystemParameter)
	 */
	@Override
	public List<SystemParameter> findByExample(SystemParameter instance) {
		log.debug("finding SystemParameter instance by example");
		try {
			List<SystemParameter> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.system.SystemParameter")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findByProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<SystemParameter> findByProperty(String propertyName,
			Object value) {
		log.debug("finding SystemParameter instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SystemParameter as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findByVersion(java.lang.Object)
	 */
	@Override
	public List<SystemParameter> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findByParamValue(java.lang.Object)
	 */
	@Override
	public List<SystemParameter> findByParamValue(Object paramValue) {
		return findByProperty(PARAM_VALUE, paramValue);
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findByName(java.lang.Object)
	 */
	@Override
	public List<SystemParameter> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#findAll()
	 */
	@Override
	public List<SystemParameter> findAll() {
		log.debug("finding all SystemParameter instances");
		try {
			String queryString = "from SystemParameter";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#merge(com.dreamer.domain.system.SystemParameter)
	 */
	@Override
	@CachePut(value="simpleCache",key="#detachedInstance.code")
	public SystemParameter merge(SystemParameter detachedInstance) {
		log.debug("merging SystemParameter instance");
		try {
			SystemParameter result = (SystemParameter) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#attachDirty(com.dreamer.domain.system.SystemParameter)
	 */
	@Override
	public void attachDirty(SystemParameter instance) {
		log.debug("attaching dirty SystemParameter instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.SystemParameterDAOInf#attachClean(com.dreamer.domain.system.SystemParameter)
	 */
	@Override
	public void attachClean(SystemParameter instance) {
		log.debug("attaching clean SystemParameter instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SystemParameterDAOInf getFromApplicationContext(
			ApplicationContext ctx) {
		return (SystemParameterDAOInf) ctx.getBean("systemParameterDAO");
	}
}