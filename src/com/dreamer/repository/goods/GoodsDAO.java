package com.dreamer.repository.goods;

import com.dreamer.domain.goods.Goods;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


@Repository("goodsDAO")
public class GoodsDAO extends HibernateBaseDAO<Goods>{
	private static final Logger log = LoggerFactory.getLogger(GoodsDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String ORDER = "order";
	public static final String CURRENT_BALANCE = "currentBalance";
	public static final String CURRENT_STOCK = "currentStock";
	public static final String VERSION = "version";
	public static final String POINT_FACTOR = "pointFactor";
	public static final String CURRENT_POINT = "currentPoint";



	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	
	public List<Object[]> queryStockstatistics(String goodsName){
		try {
			String queryString = "select d.id,d.name,d.current_stock,d.current_balance,ifnull(c.zrk,0),ifnull(e.yfh,0),ifnull(f.dlye,0)"
					+ " from (select id, name,current_stock,current_balance from dreamer.goods) d left outer join "
					+ " (SELECT goods, sum(stock_change) as zrk FROM dreamer.stock_blotter a group by goods) c on d.id=c.goods left outer join "
					+ " (SELECT sum(a.quantity) as yfh,a.goods FROM dreamer.delivery_item a inner join  dreamer.delivery_note b on a.delivery_note=b.id where b.status='DELIVERY' group by a.goods) e "
					+ " on d.id=e.goods left outer join "
					+ " (select sum(current_balance) as dlye,goods from goods_account a inner join user b "
					+ " on a.user=b.id where b.identity=1 group by goods) f on d.id=f.goods";
			if(Objects.nonNull(goodsName)){
				queryString=queryString+" where d.name=:goodsName";
			}
			Query queryObject = getCurrentSession().createSQLQuery(queryString);
			if(Objects.nonNull(goodsName)){
				queryObject.setParameter("goodsName", goodsName);
			}
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("查询库存统计数据失败", re);
			throw re;
		}
	}
	

	@Override
	public List<Goods> searchEntityByPage(SearchParameter<Goods> p,
			Function<SearchParameter<Goods>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getCurrentSession()
					.createCriteria(Goods.class);
			criteria.add(example);
			criteria.addOrder(Order.asc("goodsType"));
			criteria.addOrder(Order.asc("order"));
			return criteria;
		}, getCacheQueries);
	}
	
	public List<Goods> search(SearchParameter<Goods> p,
			Function<SearchParameter<Goods>, ? extends Object> getSQL,Integer startStock,Integer endStock,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getCurrentSession()
					.createCriteria(Goods.class);
			criteria.add(example);
			Integer tempStartStock=startStock,tempEndStock=endStock;
			if(startStock==null){
				tempStartStock=-99999999;
			}
			if(endStock==null){
				tempEndStock=999999999;
			}
			criteria.add(Restrictions.between(CURRENT_STOCK, tempStartStock, tempEndStock));
			criteria.addOrder(Order.asc("order"));
			return criteria;
		}, getCacheQueries);
	}

	public void save(Goods transientInstance) {
		log.debug("saving Goods instance");
		try {
			transientInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			transientInstance.setVersion(0);
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Goods persistentInstance) {
		log.debug("deleting Goods instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Goods findById(java.lang.Integer id) {
		log.debug("getting Goods instance with id: " + id);
		try {
			Goods instance = (Goods) getCurrentSession().get(
					"com.dreamer.domain.goods.Goods", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Goods> findByExample(Goods instance) {
		log.debug("finding Goods instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.Goods")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Goods> findByProperty(String propertyName, Object value) {
		log.debug("finding Goods instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Goods as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Goods> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<Goods> findByOrder(Object order) {
		return findByProperty(ORDER, order);
	}

	public List<Goods> findByCurrentBalance(Object currentBalance) {
		return findByProperty(CURRENT_BALANCE, currentBalance);
	}

	public List<Goods> findByCurrentStock(Object currentStock) {
		return findByProperty(CURRENT_STOCK, currentStock);
	}

	public List<Goods> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Goods> findByPointFactor(Object pointFactor) {
		return findByProperty(POINT_FACTOR, pointFactor);
	}

	public List<Goods> findByCurrentPoint(Object currentPoint) {
		return findByProperty(CURRENT_POINT, currentPoint);
	}

	public List<Goods> findAll() {
		log.debug("finding all Goods instances");
		try {
			String queryString = "from Goods";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Goods merge(Goods detachedInstance) {
		log.debug("merging Goods instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			Goods result = (Goods) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Goods instance) {
		log.debug("attaching dirty Goods instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Goods instance) {
		log.debug("attaching clean Goods instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static GoodsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (GoodsDAO) ctx.getBean("goodsDAO");
	}
}