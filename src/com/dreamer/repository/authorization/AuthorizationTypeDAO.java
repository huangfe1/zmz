package com.dreamer.repository.authorization;

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


import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.domain.goods.Goods;

@Repository("authorizationTypeDAO")
public class AuthorizationTypeDAO extends HibernateBaseDAO<AuthorizationType> {
	private static final Logger log = LoggerFactory
			.getLogger(AuthorizationTypeDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String REMARK = "remark";
	public static final String ORDER = "order";
	public static final String VERSION = "version";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	

	@Override
	public List<AuthorizationType> searchEntityByPage(
			SearchParameter<AuthorizationType> p,
			Function<SearchParameter<AuthorizationType>, ? extends Object> getSQL,
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
			if(goods!=null){
				if(goods.getId()!=null){
					criteria.createCriteria("goods").add(Restrictions.idEq(goods.getId()));
				}else{
					if(goods.getName()!=null){
						criteria.createCriteria("goods").add(Restrictions.like("name", goods.getName(), MatchMode.ANYWHERE));
					}
				}
			}
			criteria.addOrder(Order.asc("order"));
			return criteria;
		}, getCacheQueries);
	}

	public void save(AuthorizationType transientInstance) {
		log.debug("saving AuthorizationType instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(AuthorizationType persistentInstance) {
		log.debug("deleting AuthorizationType instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AuthorizationType findById(java.lang.Integer id) {
		log.debug("getting AutuorizationType instance with id: " + id);
		try {
			AuthorizationType instance = (AuthorizationType) getCurrentSession()
					.get("com.dreamer.domain.authorization.AuthorizationType",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public List<AuthorizationType> findByIds(java.lang.Integer[] ids) {
		try {
			String queryString = "from AuthorizationType as model where model.id in (:ids) order by sequence";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameterList("ids", ids);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by ids failed", re);
			throw re;
		}
	}

	public List<AuthorizationType> findMainGoodsAuthorizationType(){
		try {
			String queryString = "from AuthorizationType as model where model.goods.benchmark= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, true);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find main goods authorization type failed", re);
			throw re;
		}
	}
	
	public List<AuthorizationType> findByExample(AuthorizationType instance) {
		log.debug("finding AuthorizationType instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria(
							"com.dreamer.domain.authorization.AuthorizationType")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<AuthorizationType> findByProperty(String propertyName, Object value) {
		log.debug("finding AuthorizationType instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from AuthorizationType as model where model."
					+ propertyName + "= ? order by sequence";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<AuthorizationType>findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<AuthorizationType> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List<AuthorizationType> findByOrder(Object order) {
		return findByProperty(ORDER, order);
	}

	public List<AuthorizationType> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<AuthorizationType> findAll() {
		log.debug("finding all AutuorizationType instances");
		try {
			String queryString = "from AuthorizationType order by sequence";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public AuthorizationType merge(AuthorizationType detachedInstance) {
		log.debug("merging AuthorizationType instance");
		try {
			AuthorizationType result = (AuthorizationType) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(AuthorizationType instance) {
		log.debug("attaching dirty AuthorizationType instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AuthorizationType instance) {
		log.debug("attaching clean AuthorizationType instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AuthorizationTypeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (AuthorizationTypeDAO) ctx.getBean("authorizationTypeDAO");
	}
}