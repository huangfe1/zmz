package com.dreamer.repository.user;

import com.dreamer.domain.user.Accounts;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import java.util.Date;
import java.util.List;

@Repository("accountsDAO")
public class AccountsDAO extends HibernateBaseDAO<Accounts> {
	private static final Logger log = LoggerFactory
			.getLogger(AccountsDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String POINTS_BALANCE = "pointsBalance";
	public static final String VOUCHER_BALANCE = "voucherBalance";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Accounts transientInstance) {
		log.debug("saving Accounts instance");
		try {
			transientInstance.setUpdateTime(new Date());
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/**
	 * 查询市场所有的代金券
	 */
	public Double sumVoucher() {
		String hql="select sum(voucherBalance) from Accounts as ac where ac.user.id !=3";
		try {
		 Query q =getCurrentSession().createQuery(hql);
		return (Double) q.uniqueResult();
		} catch (RuntimeException re) {
			log.error("sumVoucher failed", re);
			throw re;
		}
	}

	public void delete(Accounts persistentInstance) {
		log.debug("deleting Accounts instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Accounts findById(java.lang.Integer id) {
		log.debug("getting Accounts instance with id: " + id);
		try {
			Accounts instance = (Accounts) getCurrentSession().get(
					"com.dreamer.domain.user.Accounts", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Accounts findByAgentId(java.lang.Integer id) {
		log.debug("getting Accounts instance with id: " + id);
		try {
			String queryString = "from Accounts as model where model.user.id=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, id);
			return (Accounts)queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Accounts instance) {
		log.debug("finding Accounts instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.user.Accounts")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Accounts instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Accounts as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findByPointsBalance(Object pointsBalance) {
		return findByProperty(POINTS_BALANCE, pointsBalance);
	}

	public List findByVoucherBalance(Object voucherBalance) {
		return findByProperty(VOUCHER_BALANCE, voucherBalance);
	}

	public List findAll() {
		log.debug("finding all Accounts instances");
		try {
			String queryString = "from Accounts";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Accounts merge(Accounts detachedInstance) {
		log.debug("merging Accounts instance");
		try {
			detachedInstance.setUpdateTime(new Date());
			Accounts result = (Accounts) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Accounts instance) {
		log.debug("attaching dirty Accounts instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Accounts instance) {
		log.debug("attaching clean Accounts instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AccountsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (AccountsDAO) ctx.getBean("AccountsDAO");
	}
}