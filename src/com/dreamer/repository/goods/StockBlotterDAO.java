package com.dreamer.repository.goods;

import java.util.List;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import com.dreamer.domain.goods.StockBlotter;

@Repository("stockBlotterDAO")
public class StockBlotterDAO extends HibernateBaseDAO<StockBlotter> {
	private static final Logger log = LoggerFactory
			.getLogger(StockBlotterDAO.class);
	// property constants
	public static final String CHANGE = "change";
	public static final String CURRENT_BALANCE = "currentBalance";
	public static final String CURRENT_STOCK = "currentStock";
	public static final String POINT = "point";
	public static final String CURRENT_POINT = "currentPoint";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	

	@Override
	public List<StockBlotter> searchEntityByPage(
			SearchParameter<StockBlotter> p,
			Function<SearchParameter<StockBlotter>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(getClazz());
			criteria.add(example);
			Goods goods=t.getEntity().getGoods();
			if(goods!=null && goods.getId()!=null){
				criteria.createCriteria("goods").add(Restrictions.idEq(goods.getId()));
			}
			criteria.addOrder(Order.asc("goods")).addOrder(Order.desc("operateTime"));
			return criteria;
		}, getCacheQueries);
	}

	public void save(StockBlotter transientInstance) {
		log.debug("saving StockBlotter instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(StockBlotter persistentInstance) {
		log.debug("deleting StockBlotter instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public StockBlotter findById(java.lang.Integer id) {
		log.debug("getting StockBlotter instance with id: " + id);
		try {
			StockBlotter instance = (StockBlotter) getCurrentSession().get(
					"com.dreamer.repository.goods.StockBlotter", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<StockBlotter> findByExample(StockBlotter instance) {
		log.debug("finding StockBlotter instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.StockBlotter")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<StockBlotter> findByProperty(String propertyName, Object value) {
		log.debug("finding StockBlotter instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from StockBlotter as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<StockBlotter> findByChange(Object change) {
		return findByProperty(CHANGE, change);
	}

	public List<StockBlotter> findByCurrentBalance(Object currentBalance) {
		return findByProperty(CURRENT_BALANCE, currentBalance);
	}

	public List<StockBlotter> findByCurrentStock(Object currentStock) {
		return findByProperty(CURRENT_STOCK, currentStock);
	}

	public List<StockBlotter> findByPoint(Object point) {
		return findByProperty(POINT, point);
	}

	public List<StockBlotter> findByCurrentPoint(Object currentPoint) {
		return findByProperty(CURRENT_POINT, currentPoint);
	}

	public List<StockBlotter> findAll() {
		log.debug("finding all StockBlotter instances");
		try {
			String queryString = "from StockBlotter";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public StockBlotter merge(StockBlotter detachedInstance) {
		log.debug("merging StockBlotter instance");
		try {
			StockBlotter result = (StockBlotter) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(StockBlotter instance) {
		log.debug("attaching dirty StockBlotter instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(StockBlotter instance) {
		log.debug("attaching clean StockBlotter instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static StockBlotterDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (StockBlotterDAO) ctx.getBean("StockBlotterDAO");
	}
}