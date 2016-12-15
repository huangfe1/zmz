package com.dreamer.view.filter;

import com.dreamer.domain.user.Agent;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.service.user.UserHandler;
import com.dreamer.view.LoginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;
@Component("loginFilter")
public final class LoginFilter implements Filter {

	private String debug;
	private String dmz;
	private final static String VMALL_LOGIN_PATH = "/vmall/uc/dmz/login.html";
	private final static String LOGIN_PATH = "/login.html";
	private final static String LOGIN_PATH_JSON = "/login.json";
	private final static String DEBUG_PARAMETER_NAME = "debug";
	private final static String DMZ_PARAMETER_NAME = "dmz";
	private final static String DMZ = "/dmz/";
	private final static String XHR_HEADER="XMLHttpRequest";
	private final static String XHR_HEADER_NAME="X-Requested-With";

	/**
	 * Default constructor.
	 */
	public LoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		// pass the request along the filter chain
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String fullRequestPath = WebUtil.getRequstPath(request);
		if (Objects.equals(debug, "1")) {
			chain.doFilter(request, response);
			return;
		}
        if(!WebUtil.isLogin(request))	autoLogin(request,response);LOG.debug("用户没登录自动登陆路径");
		if (WebUtil.isLogin(request)
				|| notNeedValidatePath(WebUtil
						.getRequstPathWithoutQueryString(request))) {
			LOG.debug("用户已登录或免检请求路径");
			chain.doFilter(request, response);
			return;
		} else {
			LOG.debug("用户未登陆请求");
			if (WebUtil.isLogin(request)){
				LOG.debug("用户自动登陆成功");
				chain.doFilter(request, response);
				return;
			}
			String xhr = request.getHeader(XHR_HEADER_NAME);
			if (fullRequestPath.indexOf("/vmall/") > -1 || fullRequestPath.indexOf("/pmall/")>-1||fullRequestPath.indexOf("/tmall/")>-1) {
				String redirectUrl = WebUtil.getContextPath(request)
						+ VMALL_LOGIN_PATH ;
				if (Objects.equals(xhr, XHR_HEADER)) {
					response.setHeader("Location", redirectUrl+ "?url="
							+ URLEncoder.encode(request.getHeader("Referer"), "UTF-8"));
					response.setHeader("Content-Type", "application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					//response.sendRedirect(redirectUrl);
					return;
				} else {
					response.sendRedirect(redirectUrl+ "?url="
							+ URLEncoder.encode(fullRequestPath, "UTF-8"));
				}
			} else {
				String redirectUrl = WebUtil.getContextPath(request)
						+ LOGIN_PATH + "?url="
						+ URLEncoder.encode(fullRequestPath, "UTF-8");
				if (Objects.equals(xhr, XHR_HEADER)) {
					response.setHeader("Location", redirectUrl);
					response.setHeader("Content-Type", "application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					//response.sendRedirect(redirectUrl);
					return;
				} else {
					response.sendRedirect(redirectUrl);
				}
			}

		}

	}

	public void autoLogin(HttpServletRequest request,HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
		Cookie userCookie=null,tokenCookie=null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					userCookie=cookie;
					LOG.debug("user:{}",cookie.getValue());
				}
				if(cookie.getName().equals("token")){
					tokenCookie=cookie;
					LOG.debug("token:{}",cookie.getName());
				}
			}
		}
		if(Objects.nonNull(userCookie) && Objects.nonNull(tokenCookie)) {
            if (Objects.nonNull(userCookie.getValue())&&Objects.nonNull(tokenCookie.getValue())) {
//                System.out.println(userHandler+"----------");
                Agent agent = userHandler.agentAutoLogin(userCookie.getValue(), tokenCookie.getValue());

                if (Objects.nonNull(agent)) {
                    LoginHelper.recordLoginInfo(request, response, agent);
                    userHandler.recordLogin(agent);
                    WebUtil.addSessionAttribute(request, Constant.MUTED_USER_KEY, mutedUserDAO.loadFirstOne());
                    LoginHelper.recordAgentLoginInfo(request, agent);
                }
            }
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		debug = fConfig.getInitParameter(DEBUG_PARAMETER_NAME);
		dmz = fConfig.getInitParameter(DMZ_PARAMETER_NAME);
		if (Objects.isNull(dmz) || dmz.isEmpty()) {
			dmz = DMZ;
		}
	}

	public void validate(HttpServletRequest request) {
		if (this.isIllegalClient(request)) {
			throw new RuntimeException("您的访问方式不正确!");
		}
	}

	private boolean isIllegalClient(HttpServletRequest request) {
		if (request.getHeader("referer") == null) {
			if (needValidatePath(request.getRequestURI())) {
				return true;
			}
		}
		return false;
	}

	private boolean needValidatePath(String uri) {
		return !notNeedValidatePath(uri);
	}

	private boolean notNeedValidatePath(String uri) {
		return uri.indexOf(dmz) > -1 || uri.indexOf(LOGIN_PATH) > -1
				|| uri.indexOf(LOGIN_PATH_JSON) > -1;
	}

	private final Logger LOG = LoggerFactory.getLogger(getClass());



	@Autowired
	private UserHandler userHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
