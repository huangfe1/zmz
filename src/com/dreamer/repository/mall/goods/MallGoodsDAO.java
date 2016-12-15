package com.dreamer.repository.mall.goods;

import com.dreamer.domain.mall.goods.MallGoods;
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

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Repository
public class MallGoodsDAO extends HibernateBaseDAO<MallGoods>{
	private static final Logger log = LoggerFactory
			.getLogger(MallGoodsDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String NAME = "name";
	public static final String SPEC = "spec";
	public static final String SHELF = "shelf";
	public static final String VOUCHER = "voucher";
	public static final String SEQUENCE = "sequence";
	public static final String PRICE = "price";
	public static final String POINT_PRICE = "pointPrice";
	public static final String MONEY_PRICE = "moneyPrice";


	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	

	@Override
	public List<MallGoods> searchEntityByPage(SearchParameter<MallGoods> p,
			Function<SearchParameter<MallGoods>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(getClazz());
			criteria.add(example);
			criteria.addOrder(Order.asc("sequence"));
			return criteria;
		}, getCacheQueries);
	}
	
	public List<MallGoods> search(SearchParameter<MallGoods> p,
			Function<SearchParameter<MallGoods>, ? extends Object> getSQL,Integer startStock,Integer endStock,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getCurrentSession()
					.createCriteria(MallGoods.class);
			criteria.add(example);
			Integer tempStartStock=startStock,tempEndStock=endStock;
			if(startStock==null){
				tempStartStock=-99999999;
			}
			if(endStock==null){
				tempEndStock=999999999;
			}
			criteria.add(Restrictions.between("stockQuantity", tempStartStock, tempEndStock));
			return criteria;
		}, getCacheQueries);
	}

	public void save(MallGoods transientInstance) {
		log.debug("saving MallGoods instance");
		try {
			transientInstance.setUpdateTime(new Date());
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}


	public void delete(MallGoods persistentInstance) {
		log.debug("deleting MallGoods instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MallGoods findById(java.lang.Integer id) {
		log.debug("getting MallGoods instance with id: " + id);
		try {
			MallGoods instance = (MallGoods) getCurrentSession().get(
					"com.dreamer.domain.mall.goods.MallGoods", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MallGoods> findByExample(MallGoods instance) {
		log.debug("finding MallGoods instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.mall.goods.MallGoods")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<MallGoods> findByProperty(String propertyName, Object value) {
		log.debug("finding MallGoods instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from MallGoods as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<MallGoods> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<MallGoods> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<MallGoods> findBySpec(Object spec) {
		return findByProperty(SPEC, spec);
	}

	public List<MallGoods> findByShelf(Object shelf) {
		return findByProperty(SHELF, shelf);
	}

	public List<MallGoods> findByVoucher(Object voucher) {
		return findByProperty(VOUCHER, voucher);
	}

	public List<MallGoods> findBySequence(Object sequence) {
		return findByProperty(SEQUENCE, sequence);
	}

	public List<MallGoods> findByPrice(Object price) {
		return findByProperty(PRICE, price);
	}

	public List<MallGoods> findByPointPrice(Object pointPrice) {
		return findByProperty(POINT_PRICE, pointPrice);
	}

	public List<MallGoods> findByMoneyPrice(Object moneyPrice) {
		return findByProperty(MONEY_PRICE, moneyPrice);
	}

	public List<MallGoods> findAll() {
		log.debug("finding all MallGoods instances");
		try {
			String queryString = "from MallGoods";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public MallGoods merge(MallGoods detachedInstance) {
		log.debug("merging MallGoods instance");
		try {
			detachedInstance.setUpdateTime(new Date());
			MallGoods result = (MallGoods) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}



	public void attachDirty(MallGoods instance) {
		log.debug("attaching dirty MallGoods instance");
		try {
			instance.setUpdateTime(new Date());
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MallGoods instance) {
		log.debug("attaching clean MallGoods instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static MallGoodsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (MallGoodsDAO) ctx.getBean("MallGoodsDAO");
	}
}