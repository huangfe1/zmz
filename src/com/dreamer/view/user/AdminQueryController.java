package com.dreamer.view.user;

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

import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.User;
import com.dreamer.domain.user.UserStatus;
import com.dreamer.repository.system.RoleDAO;
import com.dreamer.repository.user.AdminDAO;

@Controller
@RequestMapping("/user/admin")
public class AdminQueryController {
	
	@RequestMapping(value="/index.html",method=RequestMethod.GET)
	public String adminIndex(@ModelAttribute("parameter") SearchParameter<Admin> parameter,
			HttpServletRequest request,
			Model model){
		try{
			List<Admin> admins=adminDAO.searchEntityByPage(parameter, null, null);
			WebUtil.turnPage(parameter, request);
			model.addAttribute("admins", admins);
		}catch(Exception exp){
			LOG.error("管理员查询失败",exp);
			exp.printStackTrace();
		}
		
		return "/user/admin/admin_index";
	}
	
	@RequestMapping(value="/edit.html",method=RequestMethod.GET)
	public String edit(@ModelAttribute("parameter") SearchParameter<Admin> parameter,HttpServletRequest request,
			Model model){
		User user=(User)WebUtil.getCurrentUser(request);
		if(!user.isAdmin()){
			LOG.error("非管理员无权进行本操作");
			return "common/403";
		}
		Admin admin=parameter.getEntity();
		HashMap<Integer,Boolean> oldRoles=new HashMap<Integer,Boolean>();
		admin.getRoles().forEach(role->{
			oldRoles.put(role.getId(), true);
		});
		model.addAttribute("oldRoles", oldRoles);
		model.addAttribute("roles", roleDAO.findAll());
		if(parameter.getEntity().getId()==null){
			model.addAttribute("action", "新增");
		}else{
			model.addAttribute("action", "修改");
		}
		return "/user/admin/admin_edit";
	}

	@ModelAttribute("parameter")
	public SearchParameter<Admin> preprocess(@RequestParam("id")Optional<Integer> id,Model model){
		SearchParameter<Admin> parameter=new SearchParameter<Admin>();
		Admin admin=null;
		if(id.isPresent()){
			admin=adminDAO.findById(id.get());
		}else{
			admin=new Admin();
		}
		parameter.setEntity(admin);
		model.addAttribute("userStatus", UserStatus.values());
		return parameter;
	}
	
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private RoleDAO roleDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
