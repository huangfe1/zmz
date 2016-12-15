package com.dreamer.repository.goods;

import com.dreamer.domain.goods.Logistics;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import java.util.List;


@Repository("logisticsDAO")
public class LogisticsDAO extends HibernateBaseDAO<Logistics>{
	private static final Logger log = LoggerFactory.getLogger(LogisticsDAO.class);
	private Session getCurrentSession() {
		return getSessionFactory().getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}



	public List<Logistics> searchAllByPage(SearchParameter<Logistics> p) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t) -> {
			// TODO Auto-generated method stub
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.ANYWHERE);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(getClazz());
			criteria.add(example);
			return criteria;
		}, (o)-> true);
	}

	public void save(Logistics transientInstance) {
		log.debug("saving Logistics instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public Logistics findByProvinces(String provinces){
		try {
			String queryString = "from Logistics as model where model.provinces like ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, "%"+provinces+"%");
			return (Logistics) queryObject.uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by logistics failed", re);
			throw re;
		}
	}

	public void delete(Logistics persistentInstance) {
		log.debug("deleting Logistics instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Logistics findById(Integer id) {
		log.debug("getting Logistics instance with id: " + id);
		try {
			Logistics instance = (Logistics) getCurrentSession().get(
					"com.dreamer.domain.goods.Logistics", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Logistics merge(Logistics detachedInstance) {
		log.debug("merging Logistics instance");
		try {
			Logistics result = (Logistics) getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}



	public static LogisticsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (LogisticsDAO) ctx.getBean("priceDAO");
	}
}