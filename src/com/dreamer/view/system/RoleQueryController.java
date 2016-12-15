package com.dreamer.view.system;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.system.Module;
import com.dreamer.domain.system.Role;
import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.User;
import com.dreamer.repository.system.ModuleDAO;
import com.dreamer.repository.system.RoleDAO;

@Controller
@RequestMapping("/system/role")
public class RoleQueryController {
	
	@RequestMapping(value={"/index.html","/search.html"},method=RequestMethod.GET)
	public String index(@ModelAttribute("parameter") SearchParameter<Role> param,HttpServletRequest request,Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				return "common/403";
			}
			List<Role> roles=roleDAO.searchEntityByPage(param, null, null);
			WebUtil.turnPage(param, request);
			model.addAttribute("roles", roles);
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("查询角色错误",exp);
		}
		return "system/role_index";
	}
	
	@RequestMapping(value={"/edit.html","/new.html"},method=RequestMethod.GET)
	public String edit(@ModelAttribute("parameter") SearchParameter<Role> param,HttpServletRequest request,Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				return "common/403";
			}
			Admin admin=(Admin)user;
			List<Module> topModules=moduleDAO.findTopModules();
			HashMap<Integer,Boolean> oldIds=new HashMap<Integer,Boolean>();
			param.getEntity().getModules().forEach(md->{
				oldIds.put(md.getId(), true);
			});
			model.addAttribute("oldIds",oldIds);
			model.addAttribute("role", param.getEntity());
			model.addAttribute("topModules", topModules);
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入角色编辑错误",exp);
		}
		return "system/role_edit";
	}

	@ModelAttribute("parameter")
	public SearchParameter<Role> preprocess(@RequestParam(value="id",required=false) Optional<Integer> id){
		SearchParameter<Role> parameter=new SearchParameter<Role>();
		if(id.isPresent()){
			Role role=roleDAO.findById(id.get());
			parameter.setEntity(role);
		}else{
			parameter.setEntity(new Role());
		}
		return parameter;
	}
	
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private ModuleDAO moduleDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}

