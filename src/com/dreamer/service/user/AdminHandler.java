package com.dreamer.service.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.system.Role;
import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.UserStatus;
import com.dreamer.repository.system.RoleDAO;
import com.dreamer.repository.user.UserDAO;

@Service("adminHandler")
public class AdminHandler {
	

	@Transactional
	public void addAdmin(Admin operator,Admin admin,Integer[] roleIds){
		admin.setRoles(asseableRoles(roleIds));
		admin.setUserStatus(UserStatus.NORMAL);
		userDAO.save(admin);
	}
	
	@Transactional
	public void updateAdmin(Admin operator,Admin admin,Integer[] roleIds){
		admin.clearRoles();
		admin.setRoles(asseableRoles(roleIds));
		userDAO.merge(admin);
	}
	
	@Transactional
	public void lockAdmin(Admin operator,Admin admin){
		admin.lock();
		userDAO.merge(admin);
	}
	
	@Transactional
	public void unlockAdmin(Admin operator,Admin admin){
		admin.unlock();
		userDAO.merge(admin);
	}
	
	@Transactional
	public void removeAdmin(Admin operator,Admin admin){
		userDAO.delete(admin);
	}
	
	private Set<Role> asseableRoles(Integer[] roleIds){
		Set <Role> roles=new HashSet<Role>();
		if(Objects.nonNull(roleIds)){
			Stream.of(roleIds).forEach(id->{
				roles.add(roleDAO.findById(id));
			});
		}
		return roles;
	}
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private RoleDAO roleDAO;
}
