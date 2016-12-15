package com.dreamer.view.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class CaptchaController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	@RequestMapping("/StartCaptchaServlet")
	public void  StartCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
			GeetestLib gtSdk = new GeetestLib(GeetestConfig.getCaptcha_id(), GeetestConfig.getPrivate_key());

			String resStr = "{}";

			//自定义userid
			String userid = "test";

			//进行验证预处理
			int gtServerStatus = gtSdk.preProcess(userid);

			//将服务器状态设置到session中
			request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
			//将userid设置到session中
			request.getSession().setAttribute("userid", userid);

			resStr = gtSdk.getResponseStr();

			PrintWriter out = response.getWriter();
			out.println(resStr);

		}

	}