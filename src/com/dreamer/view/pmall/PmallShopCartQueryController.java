package com.dreamer.view.pmall;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.JsApiParameterFactory;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.service.user.AgentHandler;
import com.dreamer.util.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.json.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

@Controller
@RequestMapping("/pmall/shopcart")
public class PmallShopCartQueryController {


	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public String addGoodsToShopcart(
			HttpServletRequest request, Model model) {
		try {
			model.addAttribute("imgPath", TokenInfo.IMG_HEAD_PATH
					+ "/dmz/img/pmall/");
			return "pmall/shopcart/shopcart_index";
		} catch (Exception exp) {
			exp.printStackTrace();
			return "";
		}
	}

	/**
	 * 通过appid获取授权Code
	 * @param agentCode
	 * @param ref
	 * @param request
	 * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/settlement.html", method = RequestMethod.GET)
	public String settlement(
			HttpServletRequest request,HttpServletResponse response) {
		try {
			UriComponents backUrl= ServletUriComponentsBuilder
//						.fromContextPath(request).path("/pmall/shopcart/callback.html").queryParam("agent", agent.getId()).build();
						.fromContextPath(request).path("/pmall/shopcart/callback.html").build();
				LOG.debug("Get OpenId CallBack URL:{}",backUrl.toUriString());
			String uri=GetOpenIdHandler.createGetBaseOpenIdCallbackUrl(payConfig, backUrl.toUriString(), 1);
			LOG.debug("To Pay URL:{}",uri);
			response.setHeader("Location", uri);
			return "redirect:"+uri;
		} catch (Exception exp) {
			exp.printStackTrace();
			return "";
		}
	}

	/**
	 * 通过appid secrect授权code获取openid与access_token
	 * @param code
	 * @param state
	 * @param request
	 * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/callback.html", method = RequestMethod.GET)
	public String callback(
			@RequestParam(value = "code", required = false) String code,
			@RequestParam("state") String state,
			HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			String url=ServletUriComponentsBuilder.fromRequest(request).toUriString();
			LOG.debug("获取共享地址网页授权获取用户信息回调URL:{}",url);
			Agent agent=agentDAO.findById(((User)WebUtil.getCurrentUser(request)).getId());
			HashMap<String,String> map=getOpenIdHandler.getOpenId(payConfig, code);
			String openid = map.get("openid");
			LOG.debug("获取到用户openid:{}", openid);
			if(Objects.isNull(agent.getWxOpenid()) || agent.getWxOpenid().trim().isEmpty()){
                agent.setWxOpenid(openid);
                agentHandler.setWxOpenIdTo(agent, openid);
			}else {
				if(!agent.getWxOpenid().equals(openid)){
                    agent.setWxOpenid(openid);
					agentHandler.setWxOpenIdTo(agent, openid);

				}
			}
			String accessToken=map.get("access_token");
			String jsonParam=JsonUtil.mapToJsonStr(jsApiParameterFactory.buildEditAddress(payConfig, url, accessToken));
			LOG.debug("JSAPI Edit address Param：{}",jsonParam);
			model.addAttribute("jsapiParamJson",jsonParam);
			model.addAttribute("imgPath",TokenInfo.IMG_HEAD_PATH
					+ "/dmz/img/pmall/");
		} catch (Exception exp) {
			LOG.error("积分商城结算页获取用户共享地址前授权失败"+exp.getMessage(),exp.getMessage());
			exp.printStackTrace();
		}
		return "pmall/shopcart/shopcart_pay";
	}
	

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private PayConfig payConfig;
	@Autowired
	private GetOpenIdHandler getOpenIdHandler;
	@Autowired
	private AgentHandler agentHandler;
	@Autowired
	private JsApiParameterFactory jsApiParameterFactory;
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
