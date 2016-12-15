package com.dreamer.repository.user;

import com.dreamer.domain.user.Agent;
import ps.mx.otter.utils.SearchParameter;

import java.util.List;
import java.util.function.Function;

public interface AgentDAO {

	// property constants
	public static final String REAL_NAME = "realName";
	public static final String LOGIN_NAME = "loginName";
	public static final String MOBILE = "mobile";
	public static final String WEIXIN = "weixin";
	public static final String ID_CARD = "idCard";
	public static final String AGENT_CODE = "agentCode";
	public static final String PARENT = "parent";
	public static final String REMITTANCE = "remittance";
	public static final String AGENT_LEVEL = "referrer";
	public static final String AGENT_STATUS = "agentStatus";
	public static final String USER_STATUS = "userStatus";
	public static final String PASSWORD = "password";
	public static final String VERSION = "version";
	public static final String OPERATOR = "operator";
	public static final String WX_OPENID = "wxOpenid";
	public static final String NICK_NAME = "nickName";
	public static final String IDENTITY = "identity";

	public abstract Long countAgent(String idCard, String mobile, String weixin);

	public abstract Long countAgent(String mobile, String weixin);
	
	public abstract void batchGenerateSubdomain();

	public abstract List<Agent> findChildren(Agent parent);

	public abstract List<Agent> findByReferre(Agent referre);

	public abstract Long countNewer();

	public abstract List<Agent> searchEntityByPage(SearchParameter<Agent> p,
			Function<SearchParameter<Agent>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries);

	public abstract List<Agent> searchChildrenByPage(SearchParameter<Agent> p,
			Agent parent,Integer type);

	public abstract void save(Agent transientInstance);

	public abstract void delete(Agent persistentInstance);

	public abstract Agent findById(java.lang.Integer id);

	public abstract List<Agent> findByExample(Agent instance);

	public abstract List<Agent> findByProperty(String propertyName, Object value);

	public abstract List<Agent> findByRealName(Object realName);

	public abstract List<Agent> findByLoginName(Object loginName);

	public abstract List<Agent> findByMobile(Object mobile);

	public abstract List<Agent> findByWeixin(Object weixin);

	public abstract List<Agent> findByIdCard(Object idCard);

	public abstract Agent findByAgentCode(Object agentCode);
	
	public Agent findByDmz(Object agentCode);
	
	public List<Agent> searchEntityByMutilValue(SearchParameter<Agent> p,String value) ;

	public abstract Agent findByAgentCodeAndName(String agentCode, String name);

	public abstract List<Agent> findByParent(Object parent);

	public abstract List<Agent> findByRemittance(Object remittance);

	public abstract List<Agent> findByAgentLevel(Object agentLevel);

	public abstract List<Agent> findByAgentStatus(Object agentStatus);

	public abstract List<Agent> findByUserStatus(Object userStatus);

	public abstract List<Agent> findByPassword(Object password);

	public abstract List<Agent> findByVersion(Object version);

	public abstract List<Agent> findByOperator(Object operator);

	public abstract List<Agent> findByWxOpenid(Object wxOpenid);

	public abstract List<Agent> findByNickName(Object nickName);

	public abstract List<Agent> findByIdentity(Object identity);

	public abstract List<Agent> findAll();

	public abstract List<Agent> findHasOpenId();
	
	public abstract List<Object[]> findMarketByParentAgent(Integer parent,Integer level );

	public abstract Agent merge(Agent detachedInstance);

	void update(Agent detachedInstance);

	public abstract void attachDirty(Agent instance);

	public abstract void attachClean(Agent instance);

}