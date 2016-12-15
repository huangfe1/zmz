package com.dreamer.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.date.DateUtil;
import ps.mx.otter.utils.digest.DigestToolHMAC;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;

public class LoginHelper {
	
	public static void recordLoginInfo(HttpServletRequest request,HttpServletResponse response, User user){
		WebUtil.addCurrentUser(request, user);
		WebUtil.addLastVisitIp(request, user.getLastVisitIP());
		WebUtil.addLastVisitTime(request,DateUtil.date2string(user.getLastVisitTime(),DateUtil.DATE_TIME_FORMAT));
		user.setLastVisitIP(request.getRemoteAddr());
		user.setLastVisitTime(new Timestamp(System.currentTimeMillis()));
		String autoLoginToken=HMAC.generateDigest(user.getRealName(), String.valueOf(System.nanoTime()));
		String contextPath=request.getServletContext().getContextPath()+"/";
		Cookie userCookie = null;
		try {
			userCookie = new Cookie("user",URLEncoder.encode(user.getLoginName(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		userCookie.setHttpOnly(true);
		userCookie.setMaxAge(COOKIE_MAX_AGE);		
		userCookie.setPath(contextPath);
		Cookie tokenCookie=new Cookie("token",autoLoginToken);
		tokenCookie.setHttpOnly(true);
		tokenCookie.setMaxAge(COOKIE_MAX_AGE);
		tokenCookie.setPath(contextPath);
		response.addCookie(userCookie);
		response.addCookie(tokenCookie);
		user.setLoginToken(autoLoginToken);
	}
	
	public static void recordAgentLoginInfo(HttpServletRequest request,Agent agent){
		WebUtil.addSessionAttribute(request, "parent", agent.getParent());
		WebUtil.addSessionAttribute(request, "agentCode", agent.getParent().getAgentCode());
		WebUtil.addSessionAttribute(request, "owner", agent.getParent().getRealName());
		WebUtil.addSessionAttribute(request, "account", agent.getAccounts());
	}
	
	private static final DigestToolHMAC HMAC = new DigestToolHMAC();
	
	private static final Integer COOKIE_MAX_AGE=365*24*60*60;

}
