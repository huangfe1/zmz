package com.dreamer.repository.goods;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

import com.dreamer.domain.goods.SecurityCode;

@Repository
public class SecurityCodeDAO extends HibernateBaseDAO<SecurityCode> {
	private static final Logger log = LoggerFactory
			.getLogger(SecurityCodeDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String CODE = "code";
	public static final String OWNER = "owner";
	public static final String GOODS_NAME = "goodsName";
	public static final String RECORDER = "recorder";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(SecurityCode transientInstance) {
		log.debug("saving SecurityCode instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public int batchSave(List<SecurityCode> codes) {
		try {
			//Query query=getCurrentSession().createQuery("delete SecurityCode as s where s.code in (:alist)");
			//query.setParameterList("alist", codes.stream().map(s->s.getCode()).collect(Collectors.toList()));
			//query.executeUpdate();
			int count = 0;
			for (SecurityCode code : codes) {
				code.setUpdateTime(new Date());
				code.setVersion(0);
				getCurrentSession().save(code);
				if (++count % 50 == 0) {
					getCurrentSession().flush();
					getCurrentSession().clear();
				}
			}
			return count;
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}

	}
	
	public int batchRemoveAndSave(List<SecurityCode> codes,String agentCode) {
		try {
			Query query=getCurrentSession().createQuery("delete SecurityCode as s where s.code in (:alist) and s.agentCode=:agentCode");
			query.setParameterList("alist", codes.stream().map(s->s.getCode()).collect(Collectors.toList()));
			query.setParameter("agentCode", agentCode);
			query.executeUpdate();
			int count = 0;
			for (SecurityCode code : codes) {
				code.setUpdateTime(new Date());
				code.setVersion(0);
				getCurrentSession().save(code);
				if (++count % 50 == 0) {
					getCurrentSession().flush();
					getCurrentSession().clear();
				}
			}
			return count;
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}

	}

	public void delete(SecurityCode persistentInstance) {
		log.debug("deleting SecurityCode instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void deleteRange(SecurityCode persistentInstance,String start,String end) {
		log.debug("deleting SecurityCode instance");
		try {
			String rm="delete from SecurityCode as a where a.code between :start and :end and a.agentCode=:code";
			Query query=getCurrentSession().createQuery(rm);
			query.setParameter("start", start);
			query.setParameter("end", end);
			query.setParameter("code", persistentInstance.getAgentCode());
			query.executeUpdate();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SecurityCode findById(java.lang.Integer id) {
		log.debug("getting SecurityCode instance with id: " + id);
		try {
			SecurityCode instance = (SecurityCode) getCurrentSession().get(
					"com.dreamer.domain.goods.SecurityCode", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<SecurityCode> findByExample(SecurityCode instance) {
		log.debug("finding SecurityCode instance by example");
		try {
			List<SecurityCode> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.SecurityCode")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<SecurityCode> findByProperty(String propertyName, Object value) {
		log.debug("finding SecurityCode instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SecurityCode as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	
	

	@Override
	public List<SecurityCode> searchEntityByPage(
			SearchParameter<SecurityCode> p,
			Function<SearchParameter<SecurityCode>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(getClazz());
			if (Objects.nonNull(t.getStartTime())
					|| Objects.nonNull(t.getEndTime())) {
				criteria.add(Restrictions.between("updateTime",
						t.getStartTimeByDate(), t.getEndTimeByDate()));
			}
			criteria.add(example).addOrder(Order.desc("updateTime")).addOrder(Order.asc("agentCode")).addOrder(Order.asc("code"));
			return criteria;
		}, getCacheQueries);
	}

	public List<SecurityCode> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<SecurityCode> findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List<SecurityCode> findByOwner(Object owner) {
		return findByProperty(OWNER, owner);
	}

	public List<SecurityCode> findByGoodsName(Object goodsName) {
		return findByProperty(GOODS_NAME, goodsName);
	}

	public List<SecurityCode> findByRecorder(Object recorder) {
		return findByProperty(RECORDER, recorder);
	}

	public List<SecurityCode> findAll() {
		log.debug("finding all SecurityCode instances");
		try {
			String queryString = "from SecurityCode";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SecurityCode merge(SecurityCode detachedInstance) {
		log.debug("merging SecurityCode instance");
		try {
			SecurityCode result = (SecurityCode) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SecurityCode instance) {
		log.debug("attaching dirty SecurityCode instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SecurityCode instance) {
		log.debug("attaching clean SecurityCode instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SecurityCodeDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (SecurityCodeDAO) ctx.getBean("securityCodeDAO");
	}
}