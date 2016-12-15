package com.dreamer.repository.goods;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.GoodsTransferStatus;
import com.dreamer.domain.goods.Transfer;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;

@Repository("transferDAO")
public class TransferDAO extends HibernateBaseDAO<Transfer> {
	private static final Logger log = LoggerFactory
			.getLogger(TransferDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String QUANTITY = "quantity";
	public static final String STATUS = "status";
	public static final String REMITTANCE = "remittance";
	public static final String REMARK = "remark";
	public static final String POINT = "point";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public Long countNewer() {
		try {
			String queryString = "select count(1) from Transfer as model where model.status=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, GoodsTransferStatus.NEW);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}
	
	public Long countNewer(Integer fromId) {
		try {
			String queryString = "select count(1) from Transfer as model where model.status=? and"
					+ " model.userByFromAgent.id=? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, GoodsTransferStatus.NEW);
			queryObject.setParameter(1, fromId);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}

	public Long countNoConfirmApplyQuantity(Integer agentId) {
		try {
			String queryString = "select sum(quantity) from Transfer as model where model.userByFromAgent.id=? "
					+ " and model.status=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, agentId);
			queryObject.setParameter(1, GoodsTransferStatus.NEW);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count new apply quantity", re);
			throw re;
		}
	}

	public Long sumNoConfirmApplyQuantity(Integer agentId,Integer goodsId) {
		try {
			String queryString = "select sum(a.quantity) as zs from transfer_item a "
					+ " inner join transfer_goods b on a.transfer=b.id where b.from_agent=? "
					+ " and b.status=? and a.goods_id=? ";
			SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
			queryObject.setParameter(0, agentId);
			queryObject.setParameter(1, GoodsTransferStatus.NEW.name());
			queryObject.setParameter(2, goodsId);
			Object records = queryObject.addScalar("zs", StandardBasicTypes.LONG).uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count new apply quantity", re);
			throw re;
		}
	}
	
	public Long sumAllEffectiveTransferQuantity(Integer agentId,Integer goodsId) {
		try {
			String queryString = "select sum(a.quantity) as zs from transfer_item a "
					+ " inner join transfer_goods b on a.transfer=b.id where b.from_agent=? "
					+ " and b.status !=? and a.goods_id=? and b.back=0";
			SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
			queryObject.setParameter(0, agentId);
			queryObject.setParameter(1, GoodsTransferStatus.DISAGREE.name());
			queryObject.setParameter(2, goodsId);
			Object records = queryObject.addScalar("zs", StandardBasicTypes.LONG).uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count all transfer quantity", re);
			throw re;
		}
	}
	
	/**
	 * 统计指定代理指定商品的退货数
	 * @param agentId 接收退货的代理
	 * @param goodsId 商品
	 * @return
	 */
	public Long sumAllEffectiveBackTransferQuantity(Integer agentId,Integer goodsId) {
		try {
			String queryString = "select sum(a.quantity) as zs from transfer_item a "
					+ " inner join transfer_goods b on a.transfer=b.id where b.to_agent=? "
					+ " and b.status !=? and a.goods_id=? and b.back=1";
			SQLQuery queryObject = getCurrentSession().createSQLQuery(queryString);
			queryObject.setParameter(0, agentId);
			queryObject.setParameter(1, GoodsTransferStatus.DISAGREE.name());
			queryObject.setParameter(2, goodsId);
			Object records = queryObject.addScalar("zs", StandardBasicTypes.LONG).uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count all transfer quantity", re);
			throw re;
		}
	}


	public List<Transfer> searchEntityByPage(SearchParameter<Transfer> p,
			Function<SearchParameter<Transfer>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					// TODO Auto-generated method stub
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					return criteria;
				}, getCacheQueries);
	}

	

	
	
	public List<Transfer> searchEntityByPage(SearchParameter<Transfer> p,
			Function<SearchParameter<Transfer>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries, Integer agentId,
			Optional<String> applyAgent) {
		// TODO Auto-generated method stub
		return super
				.searchEntityByPage(
						p,
						(t) -> {
							Example example = Example.create(t.getEntity())
									.enableLike(MatchMode.START)
									.excludeZeroes();
							Criteria criteria = getHibernateTemplate()
									.getSessionFactory().getCurrentSession()
									.createCriteria(getClazz());
							criteria.add(example);
							/*
							 * Goods goods = t.getEntity().getGoods(); if
							 * (Objects.nonNull(goods)) { if
							 * (Objects.nonNull(goods.getId())) {
							 * criteria.createCriteria("goods").add(
							 * Restrictions.idEq(goods.getId())); } else {
							 * criteria.createCriteria("goods").add(
							 * Restrictions.like("name", goods.getName(),
							 * MatchMode.ANYWHERE)); } }
							 */

                            if (Objects.nonNull(t.getEntity().getUserByFromAgent())) {
                                criteria.add(
                                        Restrictions.eq("userByFromAgent.id",
                                                t.getEntity().getUserByFromAgent().getId()));
                            }


							if (Objects.nonNull(agentId)) {
								criteria.add(Restrictions.or(Restrictions.eq(
										"userByToAgent.id", agentId),
										Restrictions.eq("userByFromAgent.id",
												agentId)));
							}
							if (applyAgent.isPresent()) {
								criteria.createCriteria("userByToAgent").add(
										Restrictions.like("realName",
												applyAgent.get(),
												MatchMode.ANYWHERE));
							}
							if (t.getStartTime() != null
									|| t.getEndTime() != null) {
								criteria.add(Restrictions.between(
										"applyTime", t.getStartTimeByDate(),
										t.getEndTimeByDate()));
							}
							criteria.addOrder(Order.desc("updateTime"));
							return criteria;
						}, getCacheQueries);
	}

	public void save(Transfer transientInstance) {
		log.debug("saving Transfer instance");
		try {
			transientInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Transfer persistentInstance) {
		log.debug("deleting Transfer instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Transfer findById(java.lang.Integer id) {
		log.debug("getting Transfer instance with id: " + id);
		try {
			Transfer instance = (Transfer) getCurrentSession().get(
					"com.dreamer.domain.goods.Transfer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Transfer> findByExample(Transfer instance) {
		log.debug("finding Transfer instance by example");
		try {
			List<Transfer> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.Transfer")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Transfer> findByProperty(String propertyName, Object value) {
		log.debug("finding Transfer instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Transfer as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Transfer> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Transfer> findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List<Transfer> findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List<Transfer> findByRemittance(Object remittance) {
		return findByProperty(REMITTANCE, remittance);
	}

	public List<Transfer> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List<Transfer> findByPoint(Object point) {
		return findByProperty(POINT, point);
	}

	public List<Transfer> findAll() {
		log.debug("finding all Transfer instances");
		try {
			String queryString = "from Transfer";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Transfer merge(Transfer detachedInstance) {
		log.debug("merging Transfer instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			Transfer result = (Transfer) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Transfer instance) {
		log.debug("attaching dirty Transfer instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Transfer instance) {
		log.debug("attaching clean Transfer instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static TransferDAO getFromApplicationContext(ApplicationContext ctx) {
		return (TransferDAO) ctx.getBean("TransferDAO");
	}
}