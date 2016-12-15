package com.dreamer.view.user;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.user.User;
import com.dreamer.repository.user.UserDAO;
import com.dreamer.service.user.UserHandler;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message editBasicInfo(@ModelAttribute("parameter") User user,
			@RequestParam(value="newPassword",required=false) String newPassword,
			@RequestParam(value="oldPassword",required=false) String oldPassword,
			HttpServletRequest request) {
		try {
			if(Objects.nonNull(newPassword) && !newPassword.isEmpty()){
				if(!Objects.equals(user.getPassword(), oldPassword)){
					return Message.createFailedMessage("原密码不正确");
				}
				user.setPassword(newPassword);
			}
			userHandler.updateUser(user);
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}

	@RequestMapping(value = "/resetPassword.json", method = RequestMethod.POST)
	public Message resetPassword(@ModelAttribute("parameter") User user,
			HttpServletRequest request) {
		try {
			userHandler.resetPassword(user);
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}

	@ModelAttribute("parameter")
	public User preprocess(@RequestParam("id") Integer id) {
		return userDAO.findById(id);
	}

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserHandler userHandler;
}
