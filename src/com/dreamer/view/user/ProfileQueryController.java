package com.dreamer.view.user;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.user.User;
import com.dreamer.repository.user.UserDAO;

@Controller
@RequestMapping("/profile")
public class ProfileQueryController {

	@RequestMapping(value="/edit.html",method=RequestMethod.GET)
	public String editBasicInfo(@ModelAttribute("parameter")User user,HttpServletRequest request){
		User currentUser=(User)WebUtil.getCurrentUser(request);
		if(Objects.equals(currentUser.getId(),user.getId())){
			user=userDAO.findById(currentUser.getId());
		}
		return "user/profile_basic_edit";
		
	}
	
	@ModelAttribute("parameter")
	public User preprocess(@RequestParam("id") Integer id){
		return userDAO.findById(id);
	}
	
	@Autowired
	private UserDAO userDAO;
}
