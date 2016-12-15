package com.dreamer.view;

import com.dreamer.domain.user.Accounts;
import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.DeliveryNoteDAO;
import com.dreamer.repository.goods.TransferDAO;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.user.AccountsDAO;
import com.dreamer.repository.user.AdminDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.service.user.AgentHandler;
import com.dreamer.service.user.UserHandler;
import com.dreamer.view.captcha.GeetestConfig;
import com.dreamer.view.captcha.GeetestLib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.CurrentUserInfoMissingException;
import ps.mx.otter.exception.ValidateCodeFailException;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.json.JsonUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Controller
public class IndexController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = { "/", "/index.html", "/index" }, method = RequestMethod.GET)
	public String index(HttpSession session, HttpServletRequest request,Model model) {
		try {
			if (WebUtil.isLogin(request)) {
				User user=(User)WebUtil.getCurrentUser(request);
				model.addAttribute("newer", agentDAO.countNewer());
				model.addAttribute("transferNewer", transferDAO.countNewer());
				model.addAttribute("deliveryNewer", deliveryNoteDAO.countNewer(user));
				model.addAttribute("newOrder", orderDAO.countNewOrder());
				model.addAttribute("sumVoucher", accountsDAO.sumVoucher());//代金券总数
				if(user.isAgent()){
					Agent agent=agentDAO.findById(user.getId());
					model.addAttribute("transferNewer", transferDAO.countNewer(agent.getId()));
					WebUtil.addSessionAttribute(request, Constant.USER_KEY, agent);
				}
				return "index";
			}else{
				return "login";
			}
		} catch (CurrentUserInfoMissingException umep) {
			session.invalidate();
			return "login";
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("进入Index 失败",exp);
			return "common/500";
		}
	}


	

	@RequestMapping(value = "/login.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> loginAsJSON(
			@RequestParam("accountName") String name,
			@RequestParam("password") String pwd, HttpServletRequest request,HttpServletResponse response) {
		try {
			//captchaValidate(captcha,request);
			captchaValidate(request);//更换验证码
			User user = userHandler.login(name, pwd);
			LOG.debug("用户:{},登录账号：{} 验证通过.", user.getRealName(),
					user.getLoginName());
			recordLoginInfo(request,response, user);
			UriComponents ucb = ServletUriComponentsBuilder
					.fromContextPath(request).path("/index.html").build();
			LOG.debug(ucb.toUri().toString());
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucb.toUri());
			String json = JsonUtil.objectToJsonStr(user, new String[] { "id",
					"version", "weixin", "mobile", "idCard","leafModules","topModules","myModules",
					"wxOpenid", "nickName", "password", "updateTime","goodsAccounts","referrer","authorizations",
					"accounts", "operator", "userStatus", "parent" });
			return new ResponseEntity<String>(json, headers, HttpStatus.OK);
		} catch (ApplicationException aep) {
			aep.printStackTrace();
			return new ResponseEntity<String>(JsonUtil.objectToJsonStr(Message.createFailedMessage(
					aep.getMessage()),null), null,HttpStatus.UNAUTHORIZED);
		} catch (Exception exp) {
			exp.printStackTrace();
			return new ResponseEntity<String>(JsonUtil.objectToJsonStr(Message.createFailedMessage(
					exp.getMessage()),null), null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/agent/login.json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> agentLoginAsJSON(
			@RequestParam("accountName") String name,
			//@RequestParam("captcha") String captcha,
			@RequestParam("password") String pwd,Model model,HttpServletRequest request,HttpServletResponse response) {
		try {
			//captchaValidate(captcha,request);
			
			Agent user = userHandler.agentLogin(name, pwd);
			LOG.debug("用户:{},登录账号：{} 验证通过.", user.getRealName(),
					user.getLoginName());
//			Agent referrer=user.getReferrer();
			Agent parent=user.getParent();
			if(Objects.nonNull(parent)){
				model.addAttribute("agentCode",parent.getAgentCode());
			}
			recordLoginInfo(request,response, user);
//			UriComponents ucb = ServletUriComponentsBuilder
//					.fromContextPath(request).path("/dmz/vmall/index.html").build();
			UriComponents ucb = ServletUriComponentsBuilder
					.fromContextPath(request).path("/dmz/pmall/index.html").build();
			LOG.debug(ucb.toUri().toString());
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucb.toUri());
			LoginHelper.recordAgentLoginInfo(request, user);
			String json = JsonUtil.objectToJsonStr(user, new String[] { "id",
					"version", "weixin", "mobile", "idCard",
					"wxOpenid", "nickName", "password", "updateTime","goodsAccounts","referrer","authorizations",
					"accounts", "operator", "userStatus", "parent" });
			return new ResponseEntity<String>(json, headers, HttpStatus.OK);
		} catch (ApplicationException aep) {
			aep.printStackTrace();
			return new ResponseEntity<String>(JsonUtil.objectToJsonStr(Message.createFailedMessage(
					aep.getMessage()),null), null,HttpStatus.UNAUTHORIZED);
		} catch (Exception exp) {
			exp.printStackTrace();
			return new ResponseEntity<String>(JsonUtil.objectToJsonStr(Message.createFailedMessage(
					exp.getMessage()),null), null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void recordLoginInfo(HttpServletRequest request,HttpServletResponse response, User user){
		WebUtil.addSessionAttribute(request, Constant.MUTED_USER_KEY, mutedUserDAO.loadFirstOne());
		LoginHelper.recordLoginInfo(request, response, user);
		userHandler.recordLogin(user);
	}
	
	private void captchaValidate(String captcha,HttpServletRequest request){
		HttpSession session=request.getSession();
		String kaptchaExpected = (String) session
				.getAttribute(
						com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		LOG.debug("kaptchaExpected: {}",kaptchaExpected);
		if (captcha == null || kaptchaExpected == null
				|| !captcha.equalsIgnoreCase(kaptchaExpected)) {
			LOG.debug("输入验证码 {},正确验证码:{}",captcha,kaptchaExpected);
			throw new ValidateCodeFailException();
		}
	}

	private void captchaValidate(HttpServletRequest request){
		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getCaptcha_id(), GeetestConfig.getPrivate_key());

		String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
		String validate = request.getParameter(GeetestLib.fn_geetest_validate);
		String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

		//从session中获取gt-server状态
		int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

		//从session中获取userid
		String userid = (String)request.getSession().getAttribute("userid");

		int gtResult = 0;

		if (gt_server_status_code == 1) {
			//gt-server正常，向gt-server进行二次验证

			gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, userid);
		} else {
			// gt-server非正常情况下，进行failback模式验证

			gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
		}


		if (gtResult != 1) {
			throw new ValidateCodeFailException();
		}
	}

	@RequestMapping(value={"/mallmenu.html"},method=RequestMethod.GET,produces=MediaType.TEXT_HTML_VALUE)
	public String mallmenu(){
		return "mallmenu";
	}
	
	
	@RequestMapping(value={"/menu.html"},method=RequestMethod.GET,produces=MediaType.TEXT_HTML_VALUE)
	public String menu(HttpServletRequest request,Model model){
		User user=(User)WebUtil.getCurrentUser(request);
		if(user.isAdmin()){
			Admin admin=adminDAO.findById(user.getId());
			model.addAttribute("topModules", admin.getTopModules());
			model.addAttribute("myModules", admin.getMyModules());
		}
		Integer agentId=null;
		if(user.isAgent()){
			agentId=user.getId();
		}
		if(user.isAdmin()){
			agentId=((User)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY)).getId();
		}
		Accounts acs=accountsDAO.findByAgentId(agentId);
		model.addAttribute("accounts", acs);
		return "menu";
	}


	
	@RequestMapping(value={"/dmz/subdomain.html"},method=RequestMethod.GET)
	public void generateSubdomain(HttpServletRequest request,HttpServletResponse response){
		try{
			agentHandler.batchGenerateSubdomain();
			response.getWriter().println("batch generate subdomain ok");
		}catch(Exception exp){
			try {
				response.getWriter().println("failed:"+exp.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value={"/logout"},method=RequestMethod.GET,produces=MediaType.TEXT_HTML_VALUE)
	public String logout(HttpServletRequest request,HttpServletResponse response){
		WebUtil.logout(request);
		Cookie tokenCookie=new Cookie("token","1");
		tokenCookie.setHttpOnly(true);
		tokenCookie.setMaxAge(0);
		tokenCookie.setPath(request.getServletContext().getContextPath()+"/");
		response.addCookie(tokenCookie);
		return "login";
	}
	
	@RequestMapping(value={"/agent/logout"},method=RequestMethod.GET,produces=MediaType.TEXT_HTML_VALUE)
	public String agentLogout(HttpServletRequest request,HttpServletResponse response){
		WebUtil.logout(request);
		Cookie tokenCookie=new Cookie("token","1");
		tokenCookie.setHttpOnly(true);
		tokenCookie.setMaxAge(0);
		tokenCookie.setPath(request.getServletContext().getContextPath()+"/");
		response.addCookie(tokenCookie);
		UriComponents ucb = ServletUriComponentsBuilder
				.fromContextPath(request).path("/dmz/vmall/index.html").queryParam("ref", "01").build();
		return "redirect:"+ucb.toUriString();
	}
	
	@Autowired
	private UserHandler userHandler;
	
	@Autowired
	private MutedUserDAO mutedUserDAO;
	
	@Autowired
	private AgentDAO agentDAO;
	
	@Autowired
	private TransferDAO transferDAO;
	
	@Autowired
	private DeliveryNoteDAO deliveryNoteDAO;
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private AdminDAO adminDAO;
	
	@Autowired
	private AccountsDAO accountsDAO;
	
	@Autowired
	private AgentHandler agentHandler;
	

}
