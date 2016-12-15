package com.dreamer.repository.user;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.domain.user.UserStatus;
import com.dreamer.service.user.AgentDomainGenerateStrategy;

@Repository("agentDAO")
public class AgentDAOImp extends HibernateBaseDAO<Agent> implements AgentDAO {
	private static final Logger log = LoggerFactory
			.getLogger(AgentDAOImp.class);

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	@Override
	public void batchGenerateSubdomain() {
		// TODO Auto-generated method stub
		try {
			String queryString = "from Agent as model where model.subDomain is null";
			Session session = getCurrentSession();
			ScrollableResults agents = session.createQuery(queryString)
					.setCacheMode(CacheMode.IGNORE)
					.scroll(ScrollMode.FORWARD_ONLY);
			int count = 0;
			while (agents.next()) {
				Agent agent = (Agent) agents.get(0);
				agent.setSubDomain(agentDomainGenerateStrategy
						.gererateSubDomain(agent));
				if (++count % 50 == 0) {
					// flush a batch of updates and release memory:
					session.flush();
					session.clear();
				}
			}
		} catch (Exception exp) {
			log.error("batch generate subdomain error", exp);
			exp.printStackTrace();
		}
	}

	
	
	@Override
	public List<Object[]> findMarketByParentAgent(Integer parent,Integer level) {
		// TODO Auto-generated method stub
		try{
			String queryString="select f.id,f.real_name,f.agent_code,f.agent_status,minlevel from user f,"
					+ " (select min(a.level) as minlevel,c.id as uid  from agent_level a,goods_account b,user c"
					+ " where a.id=b.agent_level and b.user=c.id and c.identity=1 and c.parent=:parent"
					+ " group by uid) e where f.id=e.uid and minlevel=:minlevel";
			Query queryObject = getCurrentSession().createSQLQuery(queryString);
			queryObject.setParameter("parent", parent);
			queryObject.setParameter("minlevel", level);
			return queryObject.list();
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#countAgent(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public Long countAgent(String idCard, String mobile, String weixin) {
		try {
			String queryString = "select count(*) from Agent as model where model.idCard= ? or model.mobile=? or model.weixin=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, idCard);
			queryObject.setParameter(1, mobile);
			queryObject.setParameter(2, weixin);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}

	@Override
	public Long countAgent( String mobile, String weixin) {
		try {
			String queryString = "select count(id) from Agent as model where  model.mobile=? or model.weixin=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, mobile);
			queryObject.setParameter(1, weixin);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findChildren(com.dreamer.domain.
	 * user.Agent)
	 */
	@Override
	public List<Agent> findChildren(Agent parent) {
		log.debug("finding Agent instance with parent id {}", parent.getId());
		try {
			String queryString = "from Agent as model where model.parent.id=:parent";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("parent", parent.getId());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by parent id failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByReferre(com.dreamer.domain
	 * .user.Agent)
	 */
	@Override
	public List<Agent> findByReferre(Agent referre) {
		log.debug("finding Agent instance with parent id {}", referre.getId());
		try {
			String queryString = "from Agent as model where model.referre.id=:referre";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("referre", referre.getReferrer());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by parent id failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#countNewer()
	 */
	@Override
	public Long countNewer() {
		try {
			String queryString = "select count(1) from Agent as model where model.userStatus=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, UserStatus.NEW);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#searchEntityByPage(ps.mx.otter.utils
	 * .SearchParameter, java.util.function.Function,
	 * java.util.function.Function)
	 */
	@Override
	public List<Agent> searchEntityByPage(SearchParameter<Agent> p,
			Function<SearchParameter<Agent>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(Agent.class);
					User user = t.getEntity().getParent();
					if (user != null && user.getId() != null) {
						criteria.createCriteria("parent").add(
								Restrictions.eq("id", user.getId()));
						;
					}
					criteria.add(example);
					criteria.addOrder(Order.desc("joinDate"));
					return criteria;
				}, getCacheQueries);
	}

	@Override
	public List<Agent> searchEntityByMutilValue(SearchParameter<Agent> p,
			String value) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(Agent.class);
					Agent agent = t.getEntity();
					User user = agent.getParent();
					if (user != null && user.getId() != null) {
						System.out.println("=======没有执行");
						criteria.createCriteria("parent").add(
								Restrictions.eq("id", user.getId()));
					}
					if (Objects.nonNull(value) && !value.trim().isEmpty()) {
						criteria.add(Restrictions.or(
								Restrictions.ilike(AGENT_CODE, value,MatchMode.START),
								Restrictions.ilike(MOBILE, value,MatchMode.START),
								Restrictions.ilike(ID_CARD, value,MatchMode.START),
								Restrictions.ilike(WEIXIN, value,MatchMode.START),
								Restrictions.ilike(REAL_NAME, value,MatchMode.START)));
					}
					if (Objects.nonNull(agent.getAgentStatus())) {
						criteria.add(Restrictions.eq(AGENT_STATUS,
								agent.getAgentStatus()));
					}
					criteria.addOrder(Order.desc("joinDate"));
					return criteria;
				}, (Void) -> {
					return false;
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#searchChildrenByPage(ps.mx.otter
	 * .utils.SearchParameter, com.dreamer.domain.user.Agent)
	 */
	@Override
	public List<Agent> searchChildrenByPage(SearchParameter<Agent> p,
			Agent parent,Integer type) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(Agent.class);
					if(type==0){
						criteria.createCriteria("parent").add(
								Restrictions.eq("id", parent.getId()));
					}else {
						criteria.createCriteria("teqparent").add(
								Restrictions.eq("id", parent.getId()));
					}

					criteria.add(example);
					criteria.addOrder(Order.desc("joinDate"));
					return criteria;
				}, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#save(com.dreamer.domain.user.Agent)
	 */
	@Override
	public void save(Agent transientInstance) {
		log.debug("saving Agent instance");
		try {
			transientInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#delete(com.dreamer.domain.user.Agent
	 * )
	 */
	@Override
	public void delete(Agent persistentInstance) {
		log.debug("deleting Agent instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findById(java.lang.Integer)
	 */
	@Override
	public Agent findById(java.lang.Integer id) {
		log.debug("getting Agent instance with id: " + id);
		try {
			Agent instance = (Agent) getCurrentSession().get(
					"com.dreamer.domain.user.Agent", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByExample(com.dreamer.domain
	 * .user.Agent)
	 */
	@Override
	public List<Agent> findByExample(Agent instance) {
		log.debug("finding Agent instance by example");
		try {
			List<Agent> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.user.Agent")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public List<Agent> findByProperty(String propertyName, Object value) {
		log.debug("finding Agent instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Agent as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByRealName(java.lang.Object)
	 */
	@Override
	public List<Agent> findByRealName(Object realName) {
		return findByProperty(REAL_NAME, realName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByLoginName(java.lang.Object)
	 */
	@Override
	public List<Agent> findByLoginName(Object loginName) {
		try {
			String queryString = "from Agent as model where model.loginName= :loginName or model.agentCode=:agentCode  or model.weixin=:weixin or model.mobile=:mobile";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("loginName", loginName);
			queryObject.setParameter("agentCode", loginName);
			queryObject.setParameter("weixin", loginName);
			queryObject.setParameter("mobile", loginName);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findByMobile(java.lang.Object)
	 */
	@Override
	public List<Agent> findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findByWeixin(java.lang.Object)
	 */
	@Override
	public List<Agent> findByWeixin(Object weixin) {
		return findByProperty(WEIXIN, weixin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findByIdCard(java.lang.Object)
	 */
	@Override
	public List<Agent> findByIdCard(Object idCard) {
		return findByProperty(ID_CARD, idCard);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByAgentCode(java.lang.Object)
	 */
	@Override
	public Agent findByAgentCode(Object agentCode) {
		try {
			String queryString = "from Agent as model where model.agentCode= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, agentCode);
			if(queryObject.list().size()==0||queryObject.list()==null){
				return null;
			}
			return (Agent) queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by agentCode failed", re);
			throw re;
		}
	}

	@Override
	public Agent findByDmz(Object agentCode) {
		try {
			String queryString = "from Agent as model where model.agentCode= ? or model.weixin=? or model.idCard=? or model.mobile=? or model.realName=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, agentCode);
			queryObject.setParameter(1, agentCode);
			queryObject.setParameter(2, agentCode);
			queryObject.setParameter(3, agentCode);
			queryObject.setParameter(4, agentCode);
			return (Agent) queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by agentCode failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByAgentCodeAndName(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public Agent findByAgentCodeAndName(String agentCode, String name) {
		try {
			String queryString = "from Agent as model where model.agentCode= ? and model.realName=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, agentCode);
			queryObject.setParameter(1, name);
			return (Agent) queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by agentCode and name failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findByParent(java.lang.Object)
	 */
	@Override
	public List<Agent> findByParent(Object parent) {
		return findByProperty(PARENT, parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByRemittance(java.lang.Object)
	 */
	@Override
	public List<Agent> findByRemittance(Object remittance) {
		return findByProperty(REMITTANCE, remittance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByAgentLevel(java.lang.Object)
	 */
	@Override
	public List<Agent> findByAgentLevel(Object agentLevel) {
		return findByProperty(AGENT_LEVEL, agentLevel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByAgentStatus(java.lang.Object)
	 */
	@Override
	public List<Agent> findByAgentStatus(Object agentStatus) {
		return findByProperty(AGENT_STATUS, agentStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByUserStatus(java.lang.Object)
	 */
	@Override
	public List<Agent> findByUserStatus(Object userStatus) {
		return findByProperty(USER_STATUS, userStatus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByPassword(java.lang.Object)
	 */
	@Override
	public List<Agent> findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findByVersion(java.lang.Object)
	 */
	@Override
	public List<Agent> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByOperator(java.lang.Object)
	 */
	@Override
	public List<Agent> findByOperator(Object operator) {
		return findByProperty(OPERATOR, operator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByWxOpenid(java.lang.Object)
	 */
	@Override
	public List<Agent> findByWxOpenid(Object wxOpenid) {
		return findByProperty(WX_OPENID, wxOpenid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByNickName(java.lang.Object)
	 */
	@Override
	public List<Agent> findByNickName(Object nickName) {
		return findByProperty(NICK_NAME, nickName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#findByIdentity(java.lang.Object)
	 */
	@Override
	public List<Agent> findByIdentity(Object identity) {
		return findByProperty(IDENTITY, identity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamer.repository.user.AgentDAO#findAll()
	 */
	@Override
	public List<Agent> findAll() {
		log.debug("finding all Agent instances");
		try {
			String queryString = "from Agent";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	@Override
	public List<Agent> findHasOpenId() {
		try {
			String queryString = "from Agent a where  a.wxOpenid !=null";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/*
         * (non-Javadoc)
         *
         * @see
         * com.dreamer.repository.user.AgentDAO#merge(com.dreamer.domain.user.Agent)
         */
	@Override
	public Agent merge(Agent detachedInstance) {
		log.debug("merging Agent instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			Agent result = (Agent) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void update(Agent detachedInstance) {
		log.debug("update Agent instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			getCurrentSession().update(detachedInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#attachDirty(com.dreamer.domain.user
	 * .Agent)
	 */
	@Override
	public void attachDirty(Agent instance) {
		log.debug("attaching dirty Agent instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamer.repository.user.AgentDAO#attachClean(com.dreamer.domain.user
	 * .Agent)
	 */
	@Override
	public void attachClean(Agent instance) {
		log.debug("attaching clean Agent instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	@Autowired
	private AgentDomainGenerateStrategy agentDomainGenerateStrategy;

	public static AgentDAO getFromApplicationContext(ApplicationContext ctx) {
		return (AgentDAO) ctx.getBean("agentDAO");
	}
}