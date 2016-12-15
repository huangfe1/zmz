package com.dreamer.view.system;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.system.Role;
import com.dreamer.domain.user.User;
import com.dreamer.repository.system.RoleDAO;
import com.dreamer.service.system.RoleHandler;

@RestController
@RequestMapping("/system/role")
public class RoleController {
	
	
	@RequestMapping(value={"/edit.json","/save.json"},method={RequestMethod.POST,RequestMethod.PUT})
	public Message edit(@ModelAttribute("role") Role role,
			@RequestParam("modulesIds") Integer[] moduleIds,
			HttpServletRequest request,Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				return Message.createFailedMessage("非管理员无权进行本操作");
			}
			if(role.getId()==null){
				roleHandler.addRole(role,moduleIds);
			}else{
				roleHandler.updateRole(role,moduleIds);
			}
			return Message.createSuccessMessage("角色保存成功");
		}catch(Exception exp){
			exp.printStackTrace();
			return Message.createFailedMessage("角色保存失败,"+exp.getMessage());
		}
		
	}
	
	@RequestMapping(value={"/remove.json","/delete.json"},method={RequestMethod.POST,RequestMethod.DELETE})
	public Message remove(@ModelAttribute("role") Role role,
			HttpServletRequest request,Model model){
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(!user.isAdmin()){
				return Message.createFailedMessage("非管理员无权进行本操作");
			}
			roleHandler.removeRole(role);
			return Message.createSuccessMessage("角色保存成功");
		}catch(Exception exp){
			exp.printStackTrace();
			return Message.createFailedMessage("角色保存失败,"+exp.getMessage());
		}
		
	}


	@ModelAttribute("role")
	public Role preprocess(@RequestParam(value="id",required=false) Optional<Integer> id){
		Role parameter=null;
		if(id.isPresent()){
			parameter=roleDAO.findById(id.get());
		}else{
			parameter=new Role();
		}
		return parameter;
	}
	
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private RoleHandler roleHandler;
}
