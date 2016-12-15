package com.dreamer.repository.mall.goods;

import java.util.List;
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

import com.dreamer.domain.mall.goods.MallGoods;
import com.dreamer.domain.mall.goods.MallGoodsStockBlotter;

@Repository("mallGoodsStockBlotterDAO")
public class MallGoodsStockBlotterDAO extends HibernateBaseDAO<MallGoodsStockBlotter> {
	private static final Logger log = LoggerFactory
			.getLogger(MallGoodsStockBlotterDAO.class);
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
	public List<MallGoodsStockBlotter> searchEntityByPage(
			SearchParameter<MallGoodsStockBlotter> p,
			Function<SearchParameter<MallGoodsStockBlotter>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(getClazz());
			criteria.add(example);
			MallGoods goods=t.getEntity().getGoods();
			if(goods!=null && goods.getId()!=null){
				criteria.createCriteria("goods").add(Restrictions.idEq(goods.getId()));
			}
			criteria.addOrder(Order.asc("goods")).addOrder(Order.desc("operateTime"));
			return criteria;
		}, getCacheQueries);
	}

	public void save(MallGoodsStockBlotter transientInstance) {
		log.debug("saving MallGoodsStockBlotter instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MallGoodsStockBlotter persistentInstance) {
		log.debug("deleting MallGoodsStockBlotter instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MallGoodsStockBlotter findById(java.lang.Integer id) {
		log.debug("getting MallGoodsStockBlotter instance with id: " + id);
		try {
			MallGoodsStockBlotter instance = (MallGoodsStockBlotter) getCurrentSession().get(
					"com.dreamer.repository.goods.MallGoodsStockBlotter", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MallGoodsStockBlotter> findByExample(MallGoodsStockBlotter instance) {
		log.debug("finding MallGoodsStockBlotter instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.MallGoodsStockBlotter")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<MallGoodsStockBlotter> findByProperty(String propertyName, Object value) {
		log.debug("finding MallGoodsStockBlotter instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from MallGoodsStockBlotter as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<MallGoodsStockBlotter> findByChange(Object change) {
		return findByProperty(CHANGE, change);
	}

	public List<MallGoodsStockBlotter> findByCurrentBalance(Object currentBalance) {
		return findByProperty(CURRENT_BALANCE, currentBalance);
	}

	public List<MallGoodsStockBlotter> findByCurrentStock(Object currentStock) {
		return findByProperty(CURRENT_STOCK, currentStock);
	}

	public List<MallGoodsStockBlotter> findByPoint(Object point) {
		return findByProperty(POINT, point);
	}

	public List<MallGoodsStockBlotter> findByCurrentPoint(Object currentPoint) {
		return findByProperty(CURRENT_POINT, currentPoint);
	}

	public List<MallGoodsStockBlotter> findAll() {
		log.debug("finding all MallGoodsStockBlotter instances");
		try {
			String queryString = "from MallGoodsStockBlotter";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MallGoodsStockBlotter merge(MallGoodsStockBlotter detachedInstance) {
		log.debug("merging MallGoodsStockBlotter instance");
		try {
			MallGoodsStockBlotter result = (MallGoodsStockBlotter) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(MallGoodsStockBlotter instance) {
		log.debug("attaching dirty MallGoodsStockBlotter instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MallGoodsStockBlotter instance) {
		log.debug("attaching clean MallGoodsStockBlotter instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static MallGoodsStockBlotterDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (MallGoodsStockBlotterDAO) ctx.getBean("StockBlotterDAO");
	}
}