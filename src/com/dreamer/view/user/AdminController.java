package com.dreamer.view.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AdminDAO;
import com.dreamer.service.user.AdminHandler;

@RestController
@RequestMapping("/user/admin")
public class AdminController {

	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(
			@ModelAttribute("parameter") Admin parameter,
			@RequestParam("roleIds") Integer[] ids,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				if(parameter.getId()==null){
					adminHandler.addAdmin((Admin)user, parameter,ids);
				}else{
					adminHandler.updateAdmin((Admin)user, parameter,ids);
				}
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("用户信息保存失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	

	@RequestMapping(value = "/lock.json", method = RequestMethod.POST)
	public Message active(
			@ModelAttribute("parameter") Admin parameter,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				adminHandler.lockAdmin((Admin)user, parameter);
			}else if(user.isAgent()){
				return Message.createFailedMessage("无锁定操作权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("用户锁定失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/unlock.json", method = RequestMethod.POST)
	public Message reorganize(
			@ModelAttribute("parameter") Admin parameter,HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				adminHandler.unlockAdmin((Admin)user, parameter);
			}else if(user.isAgent()){
				return Message.createFailedMessage("本操作仅限于管理员权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("解锁用户失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(
			@ModelAttribute("parameter") Admin parameter,HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				adminHandler.removeAdmin((Admin)user, parameter);
			}else if(user.isAgent()){
				return Message.createFailedMessage("本操作仅限于管理员权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("删除用户信息失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	

	@ModelAttribute("parameter")
	public Admin preprocess(
			@RequestParam("id") Optional<Integer> id) {
		Admin user = null;
		if (id.isPresent()) {
			user = adminDAO.findById(id.get());
		} else {
			user = new Admin();
		}
		return user;
	}

	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private AdminHandler adminHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
