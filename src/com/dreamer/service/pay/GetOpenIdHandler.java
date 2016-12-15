package com.dreamer.service.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.http.HttpClient;
import ps.mx.otter.utils.json.JsonUtil;

import java.util.HashMap;

@Service
public class GetOpenIdHandler {

    /**
     * 通过 appid 与 secrect获取token
     * @param payConfig
     * @return
     */
	public HashMap<String,String> getToken(PayConfig payConfig){
		UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
				GET_TOKEN_URL).buildAndExpand(payConfig.getAppID()
				,payConfig.getSecret());
		String res = HttpClient.httpGetForString(ucb.toUriString(), null);
		LOG.debug("获取用户 token ：{}", res);
		HashMap<String, String> map = JsonUtil.objectToMap(res);
		return map;
	}

    public HashMap<String,String> getTicket(String token){
        UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
                GET_TICKET_URL).buildAndExpand(token);
        String res = HttpClient.httpGetForString(ucb.toUriString(), null);
        LOG.debug("获取用户 ticket ：{}", res);
        HashMap<String, String> map = JsonUtil.objectToMap(res);
        return map;
    }
	
	public static String createGetBaseOpenIdCallbackUrl(PayConfig payConfig,String backUrl,Integer orderId){
		UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
				GET_CODE_URL).buildAndExpand(payConfig.getAppID(),
				backUrl,"snsapi_base", orderId);
		return ucb.toUriString();
	}
	
	public static String createGetUserInfoOpenIdCallbackUrl(PayConfig payConfig,String backUrl,Integer orderId){
		UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
				GET_CODE_URL).buildAndExpand(payConfig.getAppID(),
				backUrl,"snsapi_userinfo", orderId);
		return ucb.toUriString();
	}
	
	public HashMap<String,String> getOpenId(PayConfig payConfig,String code){
		UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
				GET_OPENID_URL).buildAndExpand(payConfig.getAppID(),
				payConfig.getSecret(), code);
		LOG.debug("获取用户 openid accesstoken url：{}", ucb.toUriString());
		String res = HttpClient.httpGetForString(ucb.toUriString(), null);
		LOG.debug("获取用户 openid accesstoken ：{}", res);
		HashMap<String, String> map = JsonUtil.objectToMap(res);
		return map;
	}



    private final static String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket"
            + "?access_token={ACCESS_TOKEN}&type=jsapi";


    private final static String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token"
			+ "?grant_type=client_credential&appid={APPID}&secret={APPSECRET}";

	private final static String GET_CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize"
			+ "?appid={APPID}&redirect_uri={REDIRECT_URI}&response_type=code&scope={scope}&state={STATE}#wechat_redirect";
	
	private static final String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
			+ "appid={APPID}&secret={SECRET}&code={CODE}&grant_type=authorization_code";
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
