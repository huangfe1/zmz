package com.dreamer.repository.goods;

import com.dreamer.domain.goods.DeliveryApplyOrigin;
import com.dreamer.domain.goods.DeliveryNote;
import com.dreamer.domain.goods.DeliveryStatus;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.MutedUser;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.MutedUserDAO;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Repository("deliveryNoteDAO")
public class DeliveryNoteDAO extends HibernateBaseDAO<DeliveryNote> {
	private static final Logger log = LoggerFactory
			.getLogger(DeliveryNoteDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String MOBILE = "mobile";
	public static final String ADDRESS = "address";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";
	public static final String LOGISTICS_CODE = "logisticsCode";
	public static final String LOGISTICS_FEE = "logisticsFee";
	public static final String LOGISTICS = "logistics";
	public static final String PRINT_STATUS = "printStatus";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public Long countNewer(User user) {
		if (user.isAdmin()) {
			return countNewer();
		} else if (user.isAgent()) {
			Agent agent = (Agent) user;
			return countNewer(agent);
		} else {
			return 0L;
		}
	}

	public Long countNewer() {
		try {
			String queryString = "select count(1) from DeliveryNote as model where model.status=? ";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, DeliveryStatus.NEW);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}

	public List<DeliveryNote> findDownload(SearchParameter<DeliveryNote> t){
		Example example = Example.create(t.getEntity()).enableLike(
				MatchMode.START);
		Criteria criteria = getHibernateTemplate()
				.getSessionFactory().getCurrentSession()
				.createCriteria(getClazz(), "d");
		criteria.add(example);
		Agent applyUser = t.getEntity().getUserByApplyAgent();
		if (Objects.nonNull(applyUser)
				&& Objects.nonNull(applyUser.getRealName())
				&& !applyUser.getRealName().isEmpty()) {
			criteria.createCriteria("userByApplyAgent").add(
					Restrictions.or(Restrictions.like(
							"realName",
							applyUser.getRealName(),
							MatchMode.START),Restrictions.eq("agentCode", applyUser.getRealName())));
		}
		if (Objects.nonNull(t.getStartTime())
				|| Objects.nonNull(t.getEndTime())) {
			criteria.add(Restrictions.between("applyTime",
					t.getStartTimeByDate(), t.getEndTimeByDate()));
		}
		criteria.addOrder(Order.desc("applyTime"));
		return  criteria.list();
	}

	public Long countNewer(Agent referrer) {
		try {
			String queryString = "select count(1) from DeliveryNote as model "
					+ "where model.status=? and model.userByApplyAgent.referrer.id=? and model.applyOrigin=?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, DeliveryStatus.NEW);
			queryObject.setParameter(1, referrer.getId());
			queryObject.setParameter(2, DeliveryApplyOrigin.MALL);
			Object records = queryObject.uniqueResult();
			return records == null ? 0 : (Long) records;
		} catch (RuntimeException re) {
			log.error("find by count", re);
			throw re;
		}
	}

	@Override
	public List<DeliveryNote> searchEntityByPage(
			SearchParameter<DeliveryNote> p,
			Function<SearchParameter<DeliveryNote>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz(), "d");
					criteria.add(example);
					Agent applyUser = t.getEntity().getUserByApplyAgent();
					if (Objects.nonNull(applyUser)
							&& Objects.nonNull(applyUser.getId())) {
						criteria.createCriteria("userByApplyAgent").add(
								Restrictions.idEq(applyUser.getId()));
					} else {
						MutedUser topAgent = mutedUserDAO.loadFirstOne();
						DetachedCriteria agentCriteria = DetachedCriteria
								.forClass(Agent.class, "agent");

						agentCriteria.add(Property.forName("agent.id")
								.eqProperty("d.userByApplyAgent"));
						agentCriteria.createCriteria("parent").add(
								Restrictions.idEq(topAgent.getId()));
						criteria.add(Restrictions.or(Subqueries
								.exists(agentCriteria.setProjection(Projections
										.property("id"))), Restrictions.ne(
								"status", DeliveryStatus.NEW), Restrictions.eq(
								"applyOrigin", DeliveryApplyOrigin.SYSTEM)));
						if (Objects.nonNull(applyUser)
								&& Objects.nonNull(applyUser.getRealName())
								&& !applyUser.getRealName().isEmpty()) {
							criteria.createCriteria("userByApplyAgent").add(
									Restrictions.or(Restrictions.like(
											"realName",
											applyUser.getRealName(),
											MatchMode.START),Restrictions.eq("agentCode", applyUser.getRealName())));
						}
						/*
						 * criteria.add(Restrictions.or(Restrictions.ne("status",
						 * DeliveryStatus.NEW), Restrictions.eq( "applyOrigin",
						 * DeliveryApplyOrigin.SYSTEM)));
						 */
					}

					if (Objects.nonNull(t.getStartTime())
							|| Objects.nonNull(t.getEndTime())) {
						criteria.add(Restrictions.between("applyTime",
								t.getStartTimeByDate(), t.getEndTimeByDate()));
					}
					criteria.addOrder(Order.desc("applyTime"));
					return criteria;
				}, getCacheQueries);
	}

	public List<DeliveryNote> searchEntityForAdminByPage(
			SearchParameter<DeliveryNote> p,
			Function<SearchParameter<DeliveryNote>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					Agent applyUser = t.getEntity().getUserByApplyAgent();
					if (Objects.nonNull(applyUser)
							&& Objects.nonNull(applyUser.getId())) {
						criteria.createCriteria("userByApplyAgent").add(
								Restrictions.idEq(applyUser.getId()));
					} else {
						criteria.createCriteria("userByApplyAgent")
								.add(Restrictions.isNull("referrer"))
								.add(Restrictions.or(Restrictions.ne("status",
										DeliveryStatus.NEW)));
					}
					if (Objects.nonNull(t.getStartTime())
							|| Objects.nonNull(t.getEndTime())) {
						criteria.add(Restrictions.between("applyTime",
								t.getStartTimeByDate(), t.getEndTimeByDate()));
					}
					return criteria;
				}, getCacheQueries);
	}

	public List<DeliveryNote> searchAgentConfirmDeliveryNoteByPage(
			SearchParameter<DeliveryNote> p,
			Function<SearchParameter<DeliveryNote>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					User parentByOperator = t.getEntity().getParentByOperator();
					if (Objects.nonNull(parentByOperator)// 操作员的直接下级代理
							&& Objects.nonNull(parentByOperator.getId())) {
						criteria.createCriteria("parentByOperator")
								.add(Restrictions.eq("id",
										parentByOperator.getId()));
					}
					if (Objects.nonNull(t.getStartTime())
							|| Objects.nonNull(t.getEndTime())) {
						criteria.add(Restrictions.between("applyTime",
								t.getStartTimeByDate(), t.getEndTimeByDate()));
					}
					criteria.add(Restrictions.or(Restrictions.eq("applyOrigin",
							DeliveryApplyOrigin.MALL),Restrictions.eq("applyOrigin",
							DeliveryApplyOrigin.TMALL)));
					criteria.addOrder(Order.desc("applyTime"));
					return criteria;
				}, getCacheQueries);
	}

	public void save(DeliveryNote transientInstance) {
		log.debug("saving DeliveryNote instance");
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

	public void delete(DeliveryNote persistentInstance) {
		log.debug("deleting DeliveryNote instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DeliveryNote findById(java.lang.Integer id) {
		log.debug("getting DeliveryNote instance with id: " + id);
		try {
			DeliveryNote instance = (DeliveryNote) getCurrentSession().get(
					"com.dreamer.domain.goods.DeliveryNote", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DeliveryNote> findByExample(DeliveryNote instance) {
		log.debug("finding DeliveryNote instance by example");
		try {
			List results = getCurrentSession()
					.createCriteria("com.dreamer.domain.goods.DeliveryNote")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<DeliveryNote> findByProperty(String propertyName, Object value) {
		log.debug("finding DeliveryNote instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DeliveryNote as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<DeliveryNote> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<DeliveryNote> findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}

	public List<DeliveryNote> findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List<DeliveryNote> findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List<DeliveryNote> findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List<DeliveryNote> findByLogisticsCode(Object logisticsCode) {
		return findByProperty(LOGISTICS_CODE, logisticsCode);
	}

	public List<DeliveryNote> findByLogisticsFee(Object logisticsFee) {
		return findByProperty(LOGISTICS_FEE, logisticsFee);
	}

	public List<DeliveryNote> findByLogistics(Object logistics) {
		return findByProperty(LOGISTICS, logistics);
	}

	public List<DeliveryNote> findByPrintStatus(Object printStatus) {
		return findByProperty(PRINT_STATUS, printStatus);
	}

	public List<DeliveryNote> findAll() {
		log.debug("finding all DeliveryNote instances");
		try {
			String queryString = "from DeliveryNote";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List<DeliveryNote> findByAgent(Agent agent) {
		log.debug("finding all DeliveryNote instances");
		try {
			String queryString = "from DeliveryNote as m where m.userByApplyAgent.id=? order by applyTime desc";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, agent.getId());
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DeliveryNote merge(DeliveryNote detachedInstance) {
		log.debug("merging DeliveryNote instance");
		try {
			detachedInstance.setUpdateTime(new Timestamp(System
					.currentTimeMillis()));
			DeliveryNote result = (DeliveryNote) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DeliveryNote instance) {
		log.debug("attaching dirty DeliveryNote instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DeliveryNote instance) {
		log.debug("attaching clean DeliveryNote instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	/**
	 * 获取所有没有发货的订单
	 * @return
     */
	public List<DeliveryNote> getDeliveryNoteNews(){
		String hql="from DeliveryNote note where note.status=? or (note.status=? and note.applyOrigin=? )";
		Query query=getCurrentSession().createQuery(hql);
		query.setParameter(0,DeliveryStatus.CONFIRM);
		query.setParameter(1,DeliveryStatus.NEW);
		query.setParameter(2,DeliveryApplyOrigin.SYSTEM);
		return query.list();
	}

    /**
     * 找出没有发货订单的货物数量总数
     */
    public List<Object[]> getOrdersItemCount(){
        String sql="select g.`name` as goods_name,sum(item.quantity) as count from delivery_item as item left join goods as g on item.goods=g.id left join delivery_note as note on item.delivery_note=note.id   where note.status= ? or (note.status=? and note.origin=? )   GROUP BY g.id;";
        SQLQuery query=getCurrentSession().createSQLQuery(sql);
        query.setParameter(0,DeliveryStatus.CONFIRM.toString());
		query.setParameter(1,DeliveryStatus.NEW.toString());
		query.setParameter(2,0);
        return query.list();
    }

	@Autowired
	private MutedUserDAO mutedUserDAO;

	public static DeliveryNoteDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (DeliveryNoteDAO) ctx.getBean("DeliveryNoteDAO");
	}
}