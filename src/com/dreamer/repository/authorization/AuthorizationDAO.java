package com.dreamer.repository.authorization;

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

import com.dreamer.domain.authorization.Authorization;

@Repository("authorizationDAO")
public class AuthorizationDAO extends HibernateBaseDAO<Authorization> {
	private static final Logger log = LoggerFactory
			.getLogger(AuthorizationDAO.class);
	// property constants
	public static final String STATUS = "status";
	public static final String VERSION = "version";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Authorization transientInstance) {
		log.debug("saving Authorization instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Authorization persistentInstance) {
		log.debug("deleting Authorization instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Authorization findById(java.lang.Integer id) {
		log.debug("getting Authorization instance with id: " + id);
		try {
			Authorization instance = (Authorization) getCurrentSession().get(
					"com.dreamer.domain.authorization.Authorization", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Authorization> findByExample(Authorization instance) {
		log.debug("finding Authorization instance by example");
		try {
			List <Authorization>results = getCurrentSession()
					.createCriteria(
							"com.dreamer.domain.authorization.Authorization")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	public List<Authorization> findByIds(Integer[] ids) {
		log.debug("finding Authorization instance with ids");
		try {
			String queryString = "from Authorization as model where model.id in (:ids)";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameterList("ids", ids);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by ids failed", re);
			throw re;
		}
	}

	public List<Authorization> findByProperty(String propertyName, Object value) {
		log.debug("finding Authorization instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Authorization as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Authorization> findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List<Authorization> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Authorization> findAll() {
		log.debug("finding all Authorization instances");
		try {
			String queryString = "from Authorization";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Authorization merge(Authorization detachedInstance) {
		log.debug("merging Authorization instance");
		try {
			Authorization result = (Authorization) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Authorization instance) {
		log.debug("attaching dirty Authorization instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Authorization instance) {
		log.debug("attaching clean Authorization instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AuthorizationDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (AuthorizationDAO) ctx.getBean("authorizationDAO");
	}
}