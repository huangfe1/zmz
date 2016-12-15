package com.dreamer.repository.account;

import java.sql.Timestamp;
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




import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.User;

@Repository("goodsAccountDAO")
public class GoodsAccountDAO extends HibernateBaseDAO<GoodsAccount>{
	private static final Logger log = LoggerFactory.getLogger(GoodsAccountDAO.class);
	// property constants
	public static final String CURRENT_BALANCE = "currentBalance";
	public static final String VERSION = "version";


	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	public List<GoodsAccount> findAccountsForUser(User user){
		log.debug("finding GoodsAccount instance by user");
		try {
			String queryString = "from GoodsAccount as model where model.user.id= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, user.getId());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by user failed", re);
			throw re;
		}
	}
	
	public List<GoodsAccount> findAccountsByGoodsId(SearchParameter<GoodsAccount> p,Integer goodsId){
		log.debug("finding GoodsAccount instance by Goods id");
		try {
			return super.searchEntityByPage(p, (t)->{
				Example example = Example.create(t.getEntity()).enableLike(
						MatchMode.START);
				Criteria criteria = getHibernateTemplate().getSessionFactory()
						.getCurrentSession()
						.createCriteria(getClazz());
				criteria.add(example);
				criteria.createCriteria("goods").add(Restrictions.idEq(goodsId));
				criteria.createCriteria("user").add(Restrictions.isNotNull("parent"));
				criteria.addOrder(Order.desc("currentBalance"));
				return criteria;
			}, null);
		} catch (RuntimeException re) {
			log.error("find by goods id failed", re);
			throw re;
		}
	}
	
	
	
	@Override
	public List<GoodsAccount> searchEntityByPage(
			SearchParameter<GoodsAccount> p,
			Function<SearchParameter<GoodsAccount>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, getSQL, getCacheQueries);
	}

	public GoodsAccount loadAccountsForUserAndGoods(User user,Goods goods){
		log.debug("finding GoodsAccount instance by user");
		try {
			String queryString = "from GoodsAccount as model where model.user.id= ? and model.goods.id=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, user.getId());
			queryObject.setParameter(1, goods.getId());
			return (GoodsAccount)queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by user failed", re);
			throw re;
		}
	}

	public void save(GoodsAccount transientInstance) {
		log.debug("saving GoodsAccount instance");
		try {
			transientInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(GoodsAccount persistentInstance) {
		log.debug("deleting GoodsAccount instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public GoodsAccount findById(java.lang.Integer id) {
		log.debug("getting GoodsAccount instance with id: " + id);
		try {
			GoodsAccount instance = (GoodsAccount) getCurrentSession().get(
					"com.dreamer.domain.account.GoodsAccount", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<GoodsAccount> findByExample(GoodsAccount instance) {
		log.debug("finding GoodsAccount instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.account.GoodsAccount")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<GoodsAccount> findByProperty(String propertyName, Object value) {
		log.debug("finding GoodsAccount instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from GoodsAccount as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<GoodsAccount> findByCurrentBalance(Object currentBalance) {
		return findByProperty(CURRENT_BALANCE, currentBalance);
	}

	public List<GoodsAccount> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<GoodsAccount> findAll() {
		log.debug("finding all GoodsAccount instances");
		try {
			String queryString = "from GoodsAccount";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public GoodsAccount merge(GoodsAccount detachedInstance) {
		log.debug("merging GoodsAccount instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			GoodsAccount result = (GoodsAccount) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(GoodsAccount instance) {
		log.debug("attaching dirty GoodsAccount instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(GoodsAccount instance) {
		log.debug("attaching clean GoodsAccount instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static GoodsAccountDAO getFromApplicationContext(ApplicationContext ctx) {
		return (GoodsAccountDAO) ctx.getBean("goodsAccountDAO");
	}
}