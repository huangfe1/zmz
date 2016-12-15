package com.dreamer.repository.user;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.user.User;

@Repository("userDAO")
public class UserDAO extends HibernateBaseDAO<User> {
	private static final Logger log = LoggerFactory.getLogger(UserDAO.class);
	// property constants
	public static final String REAL_NAME = "realName";
	public static final String LOGIN_NAME = "loginName";
	public static final String MOBILE = "mobile";
	public static final String WEIXIN = "weixin";
	public static final String ID_CARD = "idCard";
	public static final String AGENT_CODE = "agentCode";
	public static final String PARENT = "parent";
	public static final String REMITTANCE = "remittance";
	public static final String AGENT_LEVEL = "agentLevel";
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

	public void save(User transientInstance) {
		log.debug("saving User instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(User persistentInstance) {
		log.debug("deleting User instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public User findById(java.lang.Integer id) {
		log.debug("getting User instance with id: " + id);
		try {
			User instance = (User) getCurrentSession().get(
					"com.dreamer.domain.user.User", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<User> findByExample(User instance) {
		log.debug("finding User instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.user.User")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<User> findByProperty(String propertyName, Object value) {
		log.debug("finding User instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from User as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			queryObject.setMaxResults(1);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<User> findByRealName(Object realName) {
		return findByProperty(REAL_NAME, realName);
	}

	public List<User> findByLoginName(Object loginName) {
	      try {
	         String queryString = "from User as model where model.loginName= :loginName or model.agentCode=:agentCode";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 queryObject.setParameter("loginName",loginName);
			 queryObject.setParameter("agentCode",loginName);
              queryObject.setMaxResults(1);
			 return queryObject.list();
	      } catch (RuntimeException re) {
	         log.error("find by property name failed", re);
	         throw re;
	      }
	}

	public List<User> findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}

	public List<User> findByWeixin(Object weixin) {
		return findByProperty(WEIXIN, weixin);
	}

	public List<User> findByIdCard(Object idCard) {
		return findByProperty(ID_CARD, idCard);
	}

	public List<User> findByAgentCode(Object agentCode) {
		return findByProperty(AGENT_CODE, agentCode);
	}

	public List<User> findByParent(Object parent) {
		return findByProperty(PARENT, parent);
	}

	public List<User> findByRemittance(Object remittance) {
		return findByProperty(REMITTANCE, remittance);
	}

	public List<User> findByAgentLevel(Object agentLevel) {
		return findByProperty(AGENT_LEVEL, agentLevel);
	}

	public List<User> findByAgentStatus(Object agentStatus) {
		return findByProperty(AGENT_STATUS, agentStatus);
	}

	public List<User> findByUserStatus(Object userStatus) {
		return findByProperty(USER_STATUS, userStatus);
	}

	public List<User> findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List<User> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<User> findByOperator(Object operator) {
		return findByProperty(OPERATOR, operator);
	}

	public List<User> findByWxOpenid(Object wxOpenid) {
		return findByProperty(WX_OPENID, wxOpenid);
	}

	public List<User> findByNickName(Object nickName) {
		return findByProperty(NICK_NAME, nickName);
	}

	public List<User> findByIdentity(Object identity) {
		return findByProperty(IDENTITY, identity);
	}

	public List<User> findAll() {
		log.debug("finding all User instances");
		try {
			String queryString = "from User";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public User merge(User detachedInstance) {
		log.debug("merging User instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			User result = (User) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(User instance) {
		log.debug("attaching dirty User instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(User instance) {
		log.debug("attaching clean User instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static UserDAO getFromApplicationContext(ApplicationContext ctx) {
		return (UserDAO) ctx.getBean("UserDAO");
	}
}