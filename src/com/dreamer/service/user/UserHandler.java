package com.dreamer.service.user;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.AuthenticationException;
import ps.mx.otter.exception.DataNotFoundException;

import java.util.List;
import java.util.Objects;

@Service
public class UserHandler {
	
	
	public User login(String loginName,String password){		
		List<User> users=userDAO.findByLoginName(loginName);
		if(users==null || users.size()<1){
			throw new DataNotFoundException("用户名不存在"+loginName);
		}
		if(users.size()>1){
			throw new AuthenticationException("用户名在系统中不唯一");
		}
		User user=users.get(0);
		if(!Objects.equals(password, user.getPassword())){
			throw new AuthenticationException("密码错误");
		}
		if(user.isAgent()){
			Agent agent=(Agent)user;
			if(agent.isInactive()){
				throw new ApplicationException("代理未激活");
			}
			if(agent.isReorganize()){
				throw new ApplicationException("代理处于整顿状态");
			}
		}
		if(user.isLocked()){
			throw new ApplicationException("用户已锁定");
		}
		if(user.isStoped()){
			throw new ApplicationException("用户已停用");
		}
		return user;
	}
	
	public Agent agentLogin(String loginName,String password){		
		List<Agent> users=agentDAO.findByLoginName(loginName);
		if(users==null || users.size()<1){
			throw new DataNotFoundException("用户名不存在"+loginName);
		}
		if(users.size()>1){
			throw new AuthenticationException("用户名在系统中不唯一");
		}
		Agent user=users.get(0);
		if(!Objects.equals(password, user.getPassword())){
			throw new AuthenticationException("密码错误");
		}
		if(user.isInactive()){
			throw new ApplicationException("代理未激活");
		}
		if(user.isReorganize()){
			throw new ApplicationException("代理处于整顿状态");
		}
		if(user.isLocked()){
			throw new ApplicationException("用户已锁定");
		}
		if(user.isStoped()){
			throw new ApplicationException("用户已停用");
		}
		return user;
	}
	
	public Agent agentAutoLogin(String loginName,String token){		
		List<Agent> users=agentDAO.findByLoginName(loginName);
		if(users==null || users.size()<1){
			return null;
		}
		Agent user=users.get(0);
		if(Objects.isNull(token) || !Objects.equals(token, user.getLoginToken())){
			return null;
		}
		return user;
	}
	
	@Transactional
	public void recordLogin(User user){
		userDAO.merge(user);
	}
	
	@Transactional
	public void updateUser(User user){
		userDAO.merge(user);
	}
	
	@Transactional
	public void resetPassword(User user){
		String newPassword=user.defaultPassword();
		if(Objects.isNull(newPassword)){
			throw new ApplicationException("生成缺省密码失败");
		}
		user.setPassword(newPassword);
		userDAO.merge(user);
	}
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private AgentDAO agentDAO;

}
