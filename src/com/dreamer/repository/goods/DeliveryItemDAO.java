package com.dreamer.repository.goods;

import com.dreamer.domain.goods.DeliveryApplyOrigin;
import com.dreamer.domain.goods.DeliveryItem;
import com.dreamer.domain.goods.DeliveryStatus;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import java.util.Collection;
import java.util.List;


@Repository
public class DeliveryItemDAO extends HibernateBaseDAO<DeliveryItem>{
	private static final Logger log = LoggerFactory
			.getLogger(DeliveryItemDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String QUANTITY = "quantity";
	public static final String POINT = "point";


	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}
	
	public Long sumNoConfirmNewApplyQuantity(Integer applyAgentId,Integer goodsId){
		try {
			String queryString = "select sum(quantity) from DeliveryItem as model where model.deliveryNote.userByApplyAgent.id=? "
					+ " and model.deliveryNote.status=? and model.goods.id=? and model.deliveryNote.applyOrigin=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, applyAgentId);
			queryObject.setParameter(1, DeliveryStatus.NEW);
			queryObject.setParameter(2, goodsId);
			queryObject.setParameter(3, DeliveryApplyOrigin.SYSTEM);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count new apply quantity", re);
			throw re;
		}
	}

	public Long sumEffectiveDeliveryQuantity(Integer applyAgentId,Integer goodsId){
		try {
			String queryString = "select sum(quantity) from DeliveryItem as model where model.deliveryNote.userByApplyAgent.id=:agentId "
					+ " and model.deliveryNote.status !=:status and model.goods.id=:goodsId";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("agentId", applyAgentId);
			queryObject.setParameter("status", DeliveryStatus.DISAGREE);
			queryObject.setParameter("goodsId", goodsId);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count new apply quantity", re);
			throw re;
		}
	}

	/**
	 * 找出所有符合条件的item
	 */
//	public List<DeliveryItem> findDeliverySum(Collection<Integer> ids){
//        String hql="from  DeliveryItem as item where item.deliveryNote.userByApplyAgent.id in (:ids)";
//        Query query=getCurrentSession().createQuery(hql);
//        query.setParameterList("ids",ids);
//        return query.list();
//	}

    public List findDeliverySum(Collection<Integer> ids){
        String sql="select sum(item.quantity) as qu,goods.id as gid from delivery_item as item left  join delivery_note  on item.delivery_note=delivery_note.id left join goods on item.goods=goods.id left join user on delivery_note.apply_agent=user.id where user.id in (:ids) group by goods.id";
        Query query=getCurrentSession().createSQLQuery(sql);
        query.setParameterList("ids",ids);
        return query.list();
    }



    public void save(DeliveryItem transientInstance) {
		log.debug("saving DeliveryItem instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DeliveryItem persistentInstance) {
		log.debug("deleting DeliveryItem instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DeliveryItem findById(java.lang.Integer id) {
		log.debug("getting DeliveryItem instance with id: " + id);
		try {
			DeliveryItem instance = (DeliveryItem) getCurrentSession().get(
					"com.dreamer.domain.goods.DeliveryItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DeliveryItem> findByExample(DeliveryItem instance) {
		log.debug("finding DeliveryItem instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.DeliveryItem")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<DeliveryItem> findByProperty(String propertyName, Object value) {
		log.debug("finding DeliveryItem instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DeliveryItem as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<DeliveryItem> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<DeliveryItem> findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List<DeliveryItem> findByPoint(Object point) {
		return findByProperty(POINT, point);
	}

	public List<DeliveryItem> findAll() {
		log.debug("finding all DeliveryItem instances");
		try {
			String queryString = "from DeliveryItem";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DeliveryItem merge(DeliveryItem detachedInstance) {
		log.debug("merging DeliveryItem instance");
		try {
			DeliveryItem result = (DeliveryItem) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DeliveryItem instance) {
		log.debug("attaching dirty DeliveryItem instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DeliveryItem instance) {
		log.debug("attaching clean DeliveryItem instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static DeliveryItemDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (DeliveryItemDAO) ctx.getBean("deliveryItemDAO");
	}
}