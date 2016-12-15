package com.dreamer.repository.user;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.exception.DataNotFoundException;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.user.MutedUser;
import com.dreamer.domain.user.User;

@Repository("mutedUserDAO")
public class MutedUserDAO extends HibernateBaseDAO<MutedUser> {
	private static final Logger log = LoggerFactory
			.getLogger(MutedUserDAO.class);
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

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	@Override
	public List<MutedUser> searchEntityByPage(SearchParameter<MutedUser> p,
			Function<SearchParameter<MutedUser>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(MutedUser.class);
					User user = t.getEntity().getParent();
					if (user != null && user.getId() != null) {
						criteria.createCriteria("parent").add(
								Restrictions.eq("id", user.getId()));
						;
					}
					criteria.add(example);
					return criteria;
				}, getCacheQueries);
	}

	public void save(MutedUser transientInstance) {
		log.debug("saving MutedUser instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MutedUser persistentInstance) {
		log.debug("deleting MutedUser instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MutedUser loadFirstOne() {
		List<MutedUser> alls = findAll();
		if (alls != null && alls.size() > 0) {
			MutedUser mutedUser= alls.get(0);
			if(Objects.isNull(mutedUser.getAccounts())){
				mutedUser.generateAccounts();
			}
			return mutedUser;
		} else {
			throw new DataNotFoundException("系统初始用户数据缺失");
		}
	}

	public MutedUser findById(java.lang.Integer id) {
		log.debug("getting MutedUser instance with id: " + id);
		try {
			MutedUser instance = (MutedUser) getCurrentSession().get(
					"com.dreamer.domain.user.MutedUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MutedUser> findByExample(MutedUser instance) {
		log.debug("finding MutedUser instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.user.MutedUser")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<MutedUser> findByProperty(String propertyName, Object value) {
		log.debug("finding MutedUser instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from MutedUser as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public MutedUser findByAgentCode(String value) {
		log.debug("finding MutedUser instance with agentCode: " + value);
		try {
			String queryString = "from MutedUser as model where model.agentCode = :agentCode";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("agentCode", value);
			return (MutedUser) queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<MutedUser> findByRealName(Object realName) {
		return findByProperty(REAL_NAME, realName);
	}

	public List<MutedUser> findByLoginName(Object loginName) {
		return findByProperty(LOGIN_NAME, loginName);
	}

	public List<MutedUser> findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}

	public List<MutedUser> findByWeixin(Object weixin) {
		return findByProperty(WEIXIN, weixin);
	}

	public List<MutedUser> findByIdCard(Object idCard) {
		return findByProperty(ID_CARD, idCard);
	}

	public List<MutedUser> findByAdminCode(Object agentCode) {
		return findByProperty(AGENT_CODE, agentCode);
	}

	public List<MutedUser> findByParent(Object parent) {
		return findByProperty(PARENT, parent);
	}

	public List<MutedUser> findByRemittance(Object remittance) {
		return findByProperty(REMITTANCE, remittance);
	}

	public List<MutedUser> findByUserStatus(Object userStatus) {
		return findByProperty(USER_STATUS, userStatus);
	}

	public List<MutedUser> findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List<MutedUser> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<MutedUser> findByOperator(Object operator) {
		return findByProperty(OPERATOR, operator);
	}

	public List<MutedUser> findByWxOpenid(Object wxOpenid) {
		return findByProperty(WX_OPENID, wxOpenid);
	}

	public List<MutedUser> findByNickName(Object nickName) {
		return findByProperty(NICK_NAME, nickName);
	}

	public List<MutedUser> findByIdentity(Object identity) {
		return findByProperty(IDENTITY, identity);
	}

	public List<MutedUser> findAll() {
		log.debug("finding all MutedUser instances");
		try {
			String queryString = "from MutedUser";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MutedUser merge(MutedUser detachedInstance) {
		log.debug("merging MutedUser instance");
		try {
			MutedUser result = (MutedUser) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(MutedUser instance) {
		log.debug("attaching dirty MutedUser instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MutedUser instance) {
		log.debug("attaching clean MutedUser instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static MutedUserDAO getFromApplicationContext(ApplicationContext ctx) {
		return (MutedUserDAO) ctx.getBean("agentDAO");
	}
}