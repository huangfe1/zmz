package com.dreamer.repository.system;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.system.Role;

import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

@Repository
public class RoleDAO  extends HibernateBaseDAO<Role>{
	     private static final Logger log = LoggerFactory.getLogger(RoleDAO.class);
		//property constants
	public static final String VERSION = "version";
	public static final String NAME = "name";
	public static final String REMARK = "remark";




    private Session getCurrentSession(){
     return getSessionFactory().getCurrentSession(); 
    }
	protected void initDao() {
		//do nothing
	}
    
    public void save(Role transientInstance) {
        log.debug("saving Role instance");
        try {
        	transientInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            getCurrentSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Role persistentInstance) {
        log.debug("deleting Role instance");
        try {
            getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Role findById( java.lang.Integer id) {
        log.debug("getting Role instance with id: " + id);
        try {
            Role instance = (Role) getCurrentSession()
                    .get("com.dreamer.domain.system.Role", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List <Role>findByExample(Role instance) {
        log.debug("finding Role instance by example");
        try {
            List results = getCurrentSession().createCriteria(Role.class).add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List<Role> findByProperty(String propertyName, Object value) {
      log.debug("finding Role instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Role as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getCurrentSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List <Role> findByVersion(Object version
	) {
		return findByProperty(VERSION, version
		);
	}
	
	public List <Role> findByName(Object name
	) {
		return findByProperty(NAME, name
		);
	}
	
	public List <Role> findByRemark(Object remark
	) {
		return findByProperty(REMARK, remark
		);
	}
	

	public List <Role> findAll() {
		log.debug("finding all Role instances");
		try {
			String queryString = "from Role";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Role merge(Role detachedInstance) {
        log.debug("merging Role instance");
        try {
        	detachedInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            Role result = (Role) getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Role instance) {
        log.debug("attaching dirty Role instance");
        try {
            getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Role instance) {
        log.debug("attaching clean Role instance");
        try {
                      	getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
          	            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static RoleDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (RoleDAO) ctx.getBean("RoleDAO");
	}
}