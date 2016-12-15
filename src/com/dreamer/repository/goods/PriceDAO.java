package com.dreamer.repository.goods;

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
import org.springframework.transaction.annotation.Transactional;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.goods.Price;
import com.dreamer.domain.user.AgentLevel;


@Repository("priceDAO")
public class PriceDAO extends HibernateBaseDAO<Price>{
	private static final Logger log = LoggerFactory.getLogger(PriceDAO.class);
	// property constants
	public static final String PRICE = "price";
	public static final String VERSION = "version";


	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	

	public List<Price> searchAllPriceUnderAgentLevelByPage(SearchParameter<Price> p,AgentLevel level) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t) -> {
			// TODO Auto-generated method stub
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(getClazz());
			criteria.add(example);
			criteria.createCriteria("agentLevel").add(Restrictions.idEq(level.getId()));
			criteria.addOrder(Order.asc("price"));
			return criteria;
		}, (o)-> true);
	}

	public void save(Price transientInstance) {
		log.debug("saving Price instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	public Price findByGoodsAndAgentLevel(Integer goods,Integer agentLevel){
		try {
			String queryString = "from Price as model where model.goods.id=? and model.agentLevel.id=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, goods);
			queryObject.setParameter(1, agentLevel);
			return (Price)queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by goods and agentLevel failed", re);
			throw re;
		}
	}

	public void delete(Price persistentInstance) {
		log.debug("deleting Price instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Price findById(java.lang.Integer id) {
		log.debug("getting Price instance with id: " + id);
		try {
			Price instance = (Price) getCurrentSession().get(
					"com.dreamer.domain.goods.Price", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Price> findByExample(Price instance) {
		log.debug("finding Price instance by example");
		try {
			List<Price> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.Price")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Price> findByProperty(String propertyName, Object value) {
		log.debug("finding Price instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Price as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Price> findByPrice(Object price) {
		return findByProperty(PRICE, price);
	}

	public List<Price> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findAll() {
		log.debug("finding all Price instances");
		try {
			String queryString = "from Price";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Price merge(Price detachedInstance) {
		log.debug("merging Price instance");
		try {
			Price result = (Price) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Price instance) {
		log.debug("attaching dirty Price instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Price instance) {
		log.debug("attaching clean Price instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PriceDAO getFromApplicationContext(ApplicationContext ctx) {
		return (PriceDAO) ctx.getBean("priceDAO");
	}
}