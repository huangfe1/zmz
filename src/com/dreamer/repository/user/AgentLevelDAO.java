package com.dreamer.repository.user;

import java.util.List;
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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.user.AgentLevel;


@Repository("agentLevelDAO")
public class AgentLevelDAO extends HibernateBaseDAO<AgentLevel>{
	private static final Logger log = LoggerFactory
			.getLogger(AgentLevelDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String PARENT = "parent";
	public static final String LEVEL = "level";
	public static final String EXEMPT_ACTIVE = "exemptActive";
	public static final String ORDER = "order";
	public static final String VERSION = "version";



	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	

	@Override
	public List<AgentLevel> searchEntityByPage(SearchParameter<AgentLevel> p,
			Function<SearchParameter<AgentLevel>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t) -> {
			// TODO Auto-generated method stub
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getCurrentSession()
					.createCriteria(AgentLevel.class);
			criteria.add(example);
			criteria.addOrder(Order.asc("level")).addOrder(Order.asc("order"));
			return criteria;
		}, getCacheQueries);
	}

	public void save(AgentLevel transientInstance) {
		log.debug("saving AgentLevel instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(AgentLevel persistentInstance) {
		log.debug("deleting AgentLevel instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AgentLevel findById(java.lang.Integer id) {
		log.debug("getting AgentLevel instance with id: " + id);
		try {
			AgentLevel instance = (AgentLevel) getCurrentSession().get(
					"com.dreamer.domain.user.AgentLevel", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<AgentLevel> findByExample(AgentLevel instance) {
		log.debug("finding AgentLevel instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.user.AgentLevel")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<AgentLevel> findByProperty(String propertyName, Object value) {
		log.debug("finding AgentLevel instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from AgentLevel as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<AgentLevel> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<AgentLevel> findByParent(Object parent) {
		return findByProperty(PARENT, parent);
	}

	public List<AgentLevel> findByLevel(Object level) {
		return findByProperty(LEVEL, level);
	}

	public List<AgentLevel> findByExemptActive(Object exemptActive) {
		return findByProperty(EXEMPT_ACTIVE, exemptActive);
	}

	public List<AgentLevel> findByOrder(Object order) {
		return findByProperty(ORDER, order);
	}

	public List<AgentLevel> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<AgentLevel> findAll() {
		log.debug("finding all AgentLevel instances");
		try {
			String queryString = "from AgentLevel order by level, sequence";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public AgentLevel merge(AgentLevel detachedInstance) {
		log.debug("merging AgentLevel instance");
		try {
			AgentLevel result = (AgentLevel) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(AgentLevel instance) {
		log.debug("attaching dirty AgentLevel instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AgentLevel instance) {
		log.debug("attaching clean AgentLevel instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AgentLevelDAO getFromApplicationContext(ApplicationContext ctx) {
		return (AgentLevelDAO) ctx.getBean("AgentLevelDAO");
	}
}