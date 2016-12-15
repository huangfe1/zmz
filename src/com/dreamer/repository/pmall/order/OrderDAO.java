package com.dreamer.repository.pmall.order;

import com.dreamer.domain.pmall.order.*;
import com.dreamer.domain.user.Agent;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Repository
public class OrderDAO extends HibernateBaseDAO<Order> {
	private static final Logger log = LoggerFactory.getLogger(OrderDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String ORDER_NO = "orderNo";
	public static final String STATUS = "status";
	public static final String CONSIGNEE = "consignee";
	public static final String SHIPPING_ADDRESS = "shippingAddress";
	public static final String POST_CODE = "postCode";
	public static final String MOBILE = "mobile";
	public static final String DISCOUNT_AMOUNT = "discountAmount";
	public static final String LOGISTICS_CODE = "logisticsCode";
	public static final String LOGISTICS_FEE = "logisticsFee";
	public static final String LOGISTICS = "logistics";
	public static final String INVOICE_TITLE = "invoiceTitle";
	public static final String INVOICE_ITEM = "invoiceItem";
	public static final String VOUCHER = "voucher";
	public static final String BENEFIT_POINTS = "benefitPoints";

	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}
	
	public Long countNewOrder(){
		try {
			String queryString = "select count(1) from Order as model where model.status=:status";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("status", OrderStatus.NEW);
			return (Long)queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	@Override
	public List<Order> searchEntityByPage(SearchParameter<Order> p,
			Function<SearchParameter<Order>, ? extends Object> getSQL,
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
					criteria.add(
							Restrictions.between("orderTime",
									p.getStartTimeByDate(),
									p.getEndTimeByDate())).addOrder(
							org.hibernate.criterion.Order.desc("orderTime"));
					return criteria;
				}, getCacheQueries);
	}



	public List<Order> findDownload(SearchParameter<Order> p) {
		// TODO Auto-generated method stub
					Example example = Example.create(p.getEntity()).enableLike(
							MatchMode.START);
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					criteria.add(
							Restrictions.between("orderTime",
									p.getStartTimeByDate(),
									p.getEndTimeByDate())).addOrder(
							org.hibernate.criterion.Order.desc("orderTime"));
					return criteria.list();
	}
	
	public List<Order> searchShippingEntityByPage(SearchParameter<Order> p,
			Function<SearchParameter<Order>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(
				p,
				(t) -> {
					// TODO Auto-generated method stub
					Example example = Example.create(t.getEntity()).enableLike(
							MatchMode.START).excludeProperty("status");
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					criteria.add(Restrictions.or(Restrictions.eq("status", OrderStatus.SHIPPED),Restrictions.and(
							Restrictions.eq("status", OrderStatus.NEW),Restrictions.or(Restrictions.eq("paymentWay", PaymentWay.COD)
							,Restrictions.eq("paymentStatus", PaymentStatus.PAID)))));
					criteria.add(
							Restrictions.between("orderTime",
									p.getStartTimeByDate(),
									p.getEndTimeByDate())).addOrder(
							org.hibernate.criterion.Order.desc("orderTime"));
					return criteria;
				}, getCacheQueries);
	}
	
	public List<Order> findLatelyThreeMonthOrdersOfAgent(Agent agent) {
		// TODO Auto-generated method stub
					Example example = Example.create(new Order()).enableLike(
							MatchMode.START).excludeProperty("status");
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					criteria.add(Restrictions.eq("user", agent));
					/*criteria.add(Restrictions.or(Restrictions.eq("status", OrderStatus.SHIPPED),Restrictions.and(
							Restrictions.eq("status", OrderStatus.NEW),Restrictions.eq("paymentWay", PaymentWay.COD))));*/
					criteria.add(
							Restrictions.ge("orderTime",
									Date.from(Instant.now().minus(Duration.ofDays(90))))).addOrder(
							org.hibernate.criterion.Order.desc("orderTime"));
					return criteria.list(); 
	}
	/**
	 * hf 修改 增加查找下级的订单
	 * @param agent
	 * @return
	 */
	public List<Order> findLatelyThreeMonthOrdersOfCustom(Agent agent) {
		// TODO Auto-generated method stub
					Example example = Example.create(new Order()).enableLike(
							MatchMode.START).excludeProperty("status");
					Criteria criteria = getHibernateTemplate()
							.getSessionFactory().getCurrentSession()
							.createCriteria(getClazz());
					criteria.add(example);
					

					criteria.add(
							Restrictions.ge("orderTime",
									Date.from(Instant.now().minus(Duration.ofDays(90))))).addOrder(
							org.hibernate.criterion.Order.desc("orderTime"));
					
					//criteria.add(Restrictions.eq("user", agent));
					/*criteria.add(Restrictions.or(Restrictions.eq("status", OrderStatus.SHIPPED),Restrictions.and(
							Restrictions.eq("status", OrderStatus.NEW),Restrictions.eq("paymentWay", PaymentWay.COD))));*/
					//内连接user表
					 criteria = criteria.createCriteria("user");
					 //记录中的parent-agent
					 criteria.add(Restrictions.eq("parent",agent));
					
					return criteria.list(); 
	}
	
	public void save(Order transientInstance) {
		log.debug("saving Order instance");
		try {
			transientInstance.setUpdateTime(Timestamp.from(Instant.now()));
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Order persistentInstance) {
		log.debug("deleting Order instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Order findById(java.lang.Integer id) {
		log.debug("getting Order instance with id: " + id);
		try {
			Order instance = (Order) getCurrentSession().get(
					"com.dreamer.domain.pmall.order.Order", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Order> findByExample(Order instance) {
		log.debug("finding Order instance by example");
		try {
			List<Order> results = getCurrentSession()
					.createCriteria("com.dreamer.domain.pmall.order.Order")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List<Order> findByProperty(String propertyName, Object value) {
		log.debug("finding Order instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Order as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public Order findNoPaidOrderByOrderNo(String orderNo) {
		try {
			String queryString = "from Order as model where model.orderNo=:orderNo and model.paymentStatus=:paymentStatus";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter("orderNo", orderNo);
			queryObject.setParameter("paymentStatus", PaymentStatus.UNPAID);
			Object ob=queryObject.uniqueResult();
			return ob==null?null:(Order)ob;
		} catch (RuntimeException re) {
			log.error("find by orderno and not paid order failed", re);
			throw re;
		}
	}

	public List<Order> findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List<Order> findByOrderNo(Object orderNo) {
		return findByProperty(ORDER_NO, orderNo);
	}
	

	public List<Order> findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List<Order> findByConsignee(Object consignee) {
		return findByProperty(CONSIGNEE, consignee);
	}

	public List<Order> findByShippingAddress(Object shippingAddress) {
		return findByProperty(SHIPPING_ADDRESS, shippingAddress);
	}

	public List<Order> findByPostCode(Object postCode) {
		return findByProperty(POST_CODE, postCode);
	}

	public List<Order> findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}

	public List<Order> findByDiscountAmount(Object discountAmount) {
		return findByProperty(DISCOUNT_AMOUNT, discountAmount);
	}

	public List<Order> findByLogisticsCode(Object logisticsCode) {
		return findByProperty(LOGISTICS_CODE, logisticsCode);
	}

	public List<Order> findByLogisticsFee(Object logisticsFee) {
		return findByProperty(LOGISTICS_FEE, logisticsFee);
	}

	public List<Order> findByLogistics(Object logistics) {
		return findByProperty(LOGISTICS, logistics);
	}

	public List<Order> findByInvoiceTitle(Object invoiceTitle) {
		return findByProperty(INVOICE_TITLE, invoiceTitle);
	}

	public List<Order> findByInvoiceItem(Object invoiceItem) {
		return findByProperty(INVOICE_ITEM, invoiceItem);
	}

	public List<Order> findByVoucher(Object voucher) {
		return findByProperty(VOUCHER, voucher);
	}

	public List<Order> findByBenefitPoints(Object benefitPoints) {
		return findByProperty(BENEFIT_POINTS, benefitPoints);
	}

	public List<Order> findAll() {
		log.debug("finding all Order instances");
		try {
			String queryString = "from Order";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}


	public Order merge(Order detachedInstance) {
		log.debug("merging Order instance");
		try {
			detachedInstance.setUpdateTime(Timestamp.from(Instant.now()));
			Order result = (Order) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Order instance) {
		log.debug("attaching dirty Order instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Order instance) {
		log.debug("attaching clean Order instance");
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
	 * 找出所有胸膜数量超过1的
	 */
	public List<OrderItem> getOrdersMoreThan1(){
		//找出所有超出1胸膜的订单
		String hql ="from OrderItem  item where goodsId=40 and quantity > 1";
		Query query= getCurrentSession().createQuery(hql);
		List<OrderItem> orderItems =query.list();
        //每一个胸膜的单上架都返利了
        return  orderItems;
	}


	/**
	 * 找出所有没有发货的订单
	 */
	public List<Order> getOrdersNews(){
		String hql ="from Order  order where order.paymentStatus=? and order.status=? ";
		Query query= getCurrentSession().createQuery(hql);
		query.setParameter(0,PaymentStatus.PAID);//已经支付
		query.setParameter(1,OrderStatus.NEW); //新订单
		List<Order> orders =query.list();
		//每一个胸膜的单上架都返利了
		return  orders;
	}


    /**
     * 找出没有发货订单的货物数量总数
     */
    public List<Object[]> getOrdersItemCount(){
        String sql="select goods.`name` as goods_name,sum(item.quantity) as count from pointsmall_order_item as item left join mall_goods as goods on item.goods_id=goods.id left join pointsmall_order as po on item.order_id=po.id   where po.status=? and  po.payment_status=?  GROUP BY goods.id;";
        SQLQuery query=getCurrentSession().createSQLQuery(sql);
        query.setParameter(0,0);
        query.setParameter(1,1);
        return query.list();

    }


	public static OrderDAO getFromApplicationContext(ApplicationContext ctx) {
		return (OrderDAO) ctx.getBean("OrderDAO");
	}
}