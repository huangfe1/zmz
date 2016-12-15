package com.dreamer.service.system;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.system.Module;
import com.dreamer.domain.system.ModuleComparator;
import com.dreamer.domain.system.Role;
import com.dreamer.repository.system.ModuleDAO;
import com.dreamer.repository.system.RoleDAO;

@Service
public class RoleHandler {

	@Transactional
	public void addRole(Role role,Integer[] moduleIds){
		role.cleanModules();
		role.setModules(assembleModules(moduleIds));
		roleDAO.save(role);
	}
	
	@Transactional
	public void updateRole(Role role,Integer[] moduleIds){
		role.cleanModules();
		role.setModules(assembleModules(moduleIds));
		roleDAO.merge(role);
	}
	
	@Transactional
	public void removeRole(Role role){
		roleDAO.delete(role);
	}
	
	private Set<Module> assembleModules(Integer[] moduleIds){
		SortedSet<Module> modules=new TreeSet<Module>(new ModuleComparator());
		if(Objects.nonNull(moduleIds)){
			for(Integer id:moduleIds){
				boolean b=modules.add(moduleDAO.findById(id));
				LOG.debug("添加角色模块{}结果 {}",id,b);
			}
		}
		return modules;
	}
	
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private ModuleDAO moduleDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
