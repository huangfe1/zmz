package com.dreamer.repository.system;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import com.dreamer.domain.system.Module;

import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

@Repository
public class ModuleDAOImp  extends HibernateBaseDAO<Module> implements ModuleDAO{
	private static final Logger log = LoggerFactory.getLogger(ModuleDAOImp.class);
		private Session getCurrentSession(){
     return getSessionFactory().getCurrentSession(); 
    }
	protected void initDao() {
		//do nothing
	}
    
    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#save(com.dreamer.domain.system.Module)
	 */
    @Override
	public void save(Module transientInstance) {
        log.debug("saving Module instance");
        try {
        	transientInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            getCurrentSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#delete(com.dreamer.domain.system.Module)
	 */
	@Override
	public void delete(Module persistentInstance) {
        log.debug("deleting Module instance");
        try {
            getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findById(java.lang.Integer)
	 */
    @Override
    //@Cacheable(value="simpleCache",key="T(com.dreamer.domain.system.Module).getName()+#id")
	public Module findById( java.lang.Integer id) {
        log.debug("getting Module instance with id: " + id);
        try {
            Module instance = (Module) getCurrentSession()
                    .get("com.dreamer.domain.system.Module", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByExample(com.dreamer.domain.system.Module)
	 */
    @Override
	public List<Module> findByExample(Module instance) {
        log.debug("finding Module instance by example");
        try {
            List results = getCurrentSession().createCriteria(Module.class) .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByProperty(java.lang.String, java.lang.Object)
	 */
    @Override
	public List <Module> findByProperty(String propertyName, Object value) {
      log.debug("finding Module instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Module as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getCurrentSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByVersion(java.lang.Object)
	 */
	@Override
	public List<Module> findByVersion(Object version
	) {
		return findByProperty(VERSION, version
		);
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByName(java.lang.Object)
	 */
	@Override
	public List<Module> findByName(Object name
	) {
		return findByProperty(NAME, name
		);
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByUrl(java.lang.Object)
	 */
	@Override
	public List<Module> findByUrl(Object url
	) {
		return findByProperty(URL, url
		);
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByLeaf(java.lang.Object)
	 */
	@Override
	public List<Module> findByLeaf(Object leaf
	) {
		return findByProperty(LEAF, leaf
		);
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findByParent(java.lang.Object)
	 */
	@Override
	public List<Module> findByParent(Object parent
	) {
		return findByProperty(PARENT, parent
		);
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findBySequence(java.lang.Object)
	 */
	@Override
	public List<Module> findBySequence(Object sequence
	) {
		return findByProperty(SEQUENCE, sequence
		);
	}
	
	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findTopModules()
	 */
	@Override
	//@Cacheable(value="simpleCache",key="T(com.dreamer.domain.system.Module).getName()+#id")
	public List<Module> findTopModules(){
		try {
			String queryString = "from Module as m where m.parent is null ";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#findAll()
	 */
	@Override
	//@Cacheable(value="simpleCache",key="T(com.dreamer.domain.system.Module).getName()+#id")
	public List<Module> findAll() {
		log.debug("finding all Module instances");
		try {
			String queryString = "from Module order by sequence asc";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#merge(com.dreamer.domain.system.Module)
	 */
    @Override
	public Module merge(Module detachedInstance) {
        log.debug("merging Module instance");
        try {
        	detachedInstance.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            Module result = (Module) getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#attachDirty(com.dreamer.domain.system.Module)
	 */
    @Override
	public void attachDirty(Module instance) {
        log.debug("attaching dirty Module instance");
        try {
            getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    /* (non-Javadoc)
	 * @see com.dreamer.repository.system.ModuleDAO#attachClean(com.dreamer.domain.system.Module)
	 */
    @Override
	public void attachClean(Module instance) {
        log.debug("attaching clean Module instance");
        try {
                      	getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
          	            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

}