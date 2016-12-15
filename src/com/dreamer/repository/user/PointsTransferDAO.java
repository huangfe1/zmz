package com.dreamer.repository.user;

import java.util.Date;
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
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.PointsTransfer;
import com.dreamer.domain.user.User;

@Repository("pointsTransferDAO")
public class PointsTransferDAO extends HibernateBaseDAO<PointsTransfer> {
	private static final Logger log = LoggerFactory
			.getLogger(PointsTransferDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String REMARK = "remark";
	public static final String POINT = "point";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	

	public List<PointsTransfer> searchEntityByPage(
			SearchParameter<PointsTransfer> p,
			Function<SearchParameter<PointsTransfer>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries,Integer agentId) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate()
					.getSessionFactory().getCurrentSession()
					.createCriteria(getClazz());
			criteria.add(example);
			if (Objects.nonNull(agentId)) {
				criteria.add(Restrictions.or(Restrictions.eq("userByToAgent.id", agentId),Restrictions.eq("userByFromAgent.id", agentId)));
			} else {
				User agentTo = t.getEntity().getUserByToAgent();
				if (Objects.nonNull(agentTo)) {
					criteria.createCriteria("userByToAgent").add(
							Restrictions.idEq(agentTo.getId()));
				}
				User agentFrom = t.getEntity().getUserByFromAgent();
				if (Objects.nonNull(agentFrom)) {
					criteria.createCriteria("userByFromAgent").add(
							Restrictions.idEq(agentFrom.getId()));
				}
			}
			if(t.getEndTime()!=null || t.getStartTime()!=null){
				criteria.add(Restrictions.between("transferTime",t.getStartTimeByDate(), t.getEndTimeByDate()));
			}			
			criteria.addOrder(Order.desc("transferTime"));
			return criteria;
		}, getCacheQueries);
	}

	public void save(PointsTransfer transientInstance) {
		log.debug("saving PointsTransfer instance");
		try {
			transientInstance.setUpdateTime(new Date());
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PointsTransfer persistentInstance) {
		log.debug("deleting PointsTransfer instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PointsTransfer findById(java.lang.Integer id) {
		log.debug("getting PointsTransfer instance with id: " + id);
		try {
			PointsTransfer instance = (PointsTransfer) getCurrentSession().get(
					"com.dreamer.domain.user.PointsTransfer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<PointsTransfer> findByExample(PointsTransfer instance) {
		log.debug("finding PointsTransfer instance by example");
		try {
			List<PointsTransfer> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.user.PointsTransfer")
					.add(Example.create(instance))
					.addOrder(Order.asc("transferTime")).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<PointsTransfer> findByProperty(String propertyName, Object value) {
		log.debug("finding PointsTransfer instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PointsTransfer as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<PointsTransfer> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<PointsTransfer> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List<PointsTransfer> findByPoint(Object point) {
		return findByProperty(POINT, point);
	}

	public List<PointsTransfer> findAll() {
		log.debug("finding all PointsTransfer instances");
		try {
			String queryString = "from PointsTransfer";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PointsTransfer merge(PointsTransfer detachedInstance) {
		log.debug("merging PointsTransfer instance");
		try {
			detachedInstance.setUpdateTime(new Date());
			PointsTransfer result = (PointsTransfer) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PointsTransfer instance) {
		log.debug("attaching dirty PointsTransfer instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PointsTransfer instance) {
		log.debug("attaching clean PointsTransfer instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PointsTransferDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (PointsTransferDAO) ctx.getBean("pointsTransferDAO");
	}
}