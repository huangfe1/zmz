package com.dreamer.repository.user;

import java.util.List;
import java.util.function.Function;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.dao.hibernate.HibernateBaseDAO;

import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.User;




@Repository("adminDAO")
public class AdminDAO extends HibernateBaseDAO<Admin> {
	     private static final Logger log = LoggerFactory.getLogger(AdminDAO.class);
		//property constants
	public static final String REAL_NAME = "realName";
	public static final String LOGIN_NAME = "loginName";
	public static final String MOBILE = "mobile";
	public static final String WEIXIN = "weixin";
	public static final String ID_CARD = "idCard";
	public static final String AGENT_CODE = "agentCode";
	public static final String PARENT = "parent";
	public static final String REMITTANCE = "remittance";
	public static final String AGENT_LEVEL = "referrer";
	public static final String AGENT_STATUS = "agentStatus";
	public static final String USER_STATUS = "userStatus";
	public static final String PASSWORD = "password";
	public static final String VERSION = "version";
	public static final String OPERATOR = "operator";
	public static final String WX_OPENID = "wxOpenid";
	public static final String NICK_NAME = "nickName";
	public static final String IDENTITY = "identity";




    private Session getCurrentSession(){
     return getSessionFactory().getCurrentSession(); 
    }
	protected void initDao() {
		//do nothing
	}
    
	
    @Override
	public List<Admin> searchEntityByPage(SearchParameter<Admin> p,
			Function<SearchParameter<Admin>, ? extends Object> getSQL,
			Function<Void, Boolean> getCacheQueries) {
		// TODO Auto-generated method stub
		return super.searchEntityByPage(p, (t)->{
			Example example = Example.create(t.getEntity()).enableLike(
					MatchMode.START);
			Criteria criteria = getHibernateTemplate().getSessionFactory()
					.getCurrentSession()
					.createCriteria(Admin.class);
			User user=t.getEntity().getParent();
			if(user!=null && user.getId() !=null){
				criteria.createCriteria("parent").add(Restrictions.eq("id", user.getId()));;
			}
			criteria.add(example);
			return criteria;
		}, getCacheQueries);
	}
    
    
	public void save(Admin transientInstance) {
        log.debug("saving Admin instance");
        try {
            getCurrentSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Admin persistentInstance) {
        log.debug("deleting Admin instance");
        try {
            getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Admin findById( java.lang.Integer id) {
        log.debug("getting Admin instance with id: " + id);
        try {
            Admin instance = (Admin) getCurrentSession()
                    .get("com.dreamer.domain.user.Admin", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Admin> findByExample(Admin instance) {
        log.debug("finding Admin instance by example");
        try {
            List results = getCurrentSession().createCriteria("com.dreamer.domain.user.Admin") .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List<Admin> findByProperty(String propertyName, Object value) {
      log.debug("finding Admin instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Admin as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getCurrentSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Admin> findByRealName(Object realName
	) {
		return findByProperty(REAL_NAME, realName
		);
	}
	
	public List<Admin> findByLoginName(Object loginName
	) {
		return findByProperty(LOGIN_NAME, loginName
		);
	}
	
	public List<Admin> findByMobile(Object mobile
	) {
		return findByProperty(MOBILE, mobile
		);
	}
	
	public List<Admin> findByWeixin(Object weixin
	) {
		return findByProperty(WEIXIN, weixin
		);
	}
	
	public List<Admin> findByIdCard(Object idCard
	) {
		return findByProperty(ID_CARD, idCard
		);
	}
	
	public List<Admin> findByAdminCode(Object agentCode
	) {
		return findByProperty(AGENT_CODE, agentCode
		);
	}
	
	public List<Admin> findByParent(Object parent
	) {
		return findByProperty(PARENT, parent
		);
	}
	
	public List<Admin> findByRemittance(Object remittance
	) {
		return findByProperty(REMITTANCE, remittance
		);
	}
	
	
	public List<Admin> findByUserStatus(Object userStatus
	) {
		return findByProperty(USER_STATUS, userStatus
		);
	}
	
	public List<Admin> findByPassword(Object password
	) {
		return findByProperty(PASSWORD, password
		);
	}
	
	public List<Admin> findByVersion(Object version
	) {
		return findByProperty(VERSION, version
		);
	}
	
	public List<Admin> findByOperator(Object operator
	) {
		return findByProperty(OPERATOR, operator
		);
	}
	
	public List<Admin> findByWxOpenid(Object wxOpenid
	) {
		return findByProperty(WX_OPENID, wxOpenid
		);
	}
	
	public List<Admin> findByNickName(Object nickName
	) {
		return findByProperty(NICK_NAME, nickName
		);
	}
	
	public List<Admin> findByIdentity(Object identity
	) {
		return findByProperty(IDENTITY, identity
		);
	}
	

	public List<Admin> findAll() {
		log.debug("finding all Admin instances");
		try {
			String queryString = "from Admin";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Admin merge(Admin detachedInstance) {
        log.debug("merging Admin instance");
        try {
            Admin result = (Admin) getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Admin instance) {
        log.debug("attaching dirty Admin instance");
        try {
            getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Admin instance) {
        log.debug("attaching clean Admin instance");
        try {
                      	getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
          	            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static AdminDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (AdminDAO) ctx.getBean("agentDAO");
	}
}