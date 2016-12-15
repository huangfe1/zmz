package com.dreamer.view.pay;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.dreamer.service.pay.PayConfig;

import ps.mx.otter.utils.http.HttpClient;
import ps.mx.otter.utils.json.JsonUtil;

@Controller
public class GetOpenIdController {
	
	public String getCode(){
		try{
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("生成获取openid code 请求错误",exp);
		}
		return null;
	}
	
	public HashMap<String,String> getOpenId(PayConfig payConfig,String code){
		UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
				GET_OPENID_URL).buildAndExpand(payConfig.getAppID(),
				payConfig.getSecret(), code);
		LOG.debug("获取用户 openid accesstoken url：{}", ucb.toUriString());
		String res = HttpClient.httpGetForString(ucb.toUriString(), null);
		HashMap<String, String> map = JsonUtil.objectToMap(res);
		return map;
	}
	
	public static void main(String[] args){
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromUriString(GET_CODE_URL);
		System.out.println(uriBuilder.buildAndExpand("wx23424234234","http://www.xowxl.com","sns_api").toUriString());
	}

	private final static String GET_CODE_URL="https://open.weixin.qq.com/connect/oauth2/authorize"
			+ "?appid={APPID}&redirect_uri={REDIRECT_URI}&response_type=code&scope={scope}&state=STATE#wechat_redirect";
	
	private final static String GET_OPENID_URL="https://api.weixin.qq.com/sns/oauth2/access_token"
			+ "?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	@Autowired
	private PayConfig config;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
