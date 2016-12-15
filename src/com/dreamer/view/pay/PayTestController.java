package com.dreamer.view.pay;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.http.HttpClient;
import ps.mx.otter.utils.json.JsonUtil;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.service.pay.RandomStringGenerator;
import com.dreamer.service.pay.Signature;
import com.dreamer.service.pay.Util;
import com.dreamer.service.pay.param.PayUnifiedOrderReqData;
import com.dreamer.service.pay.param.UnifiedOrderResData;

@Controller
public class PayTestController {

	@RequestMapping(value = "/test/app_pay/callback.html")
	public String testPay(
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value="order",required=false) Integer orderId,
			@RequestParam("state") String state, HttpServletRequest request,
			HttpServletResponse response,Model model) {
		try {
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());
			
			Order order=orderDAO.findById(orderId);
			model.addAttribute("order", order);
			UriComponents ucb = ServletUriComponentsBuilder.fromHttpUrl(
					GET_OPENID_URL).buildAndExpand(payConfig.getAppID(),
					payConfig.getSecret(), code);
			LOG.debug("获取用户 openid accesstoken url：{}", ucb.toUriString());
			String res = HttpClient.httpGetForString(ucb.toUriString(), null);
			HashMap<String, String> map = JsonUtil.objectToMap(res);
			String openid = map.get("openid");
			LOG.debug("获取到用户openid:{}", openid);
			agent.setWxOpenid(openid);
			agentDAO.merge(agent);
			PayUnifiedOrderReqData param=new PayUnifiedOrderReqData();
			param.setAppid(payConfig.getAppID());
			param.setBody("test");
			param.setOpenid(openid);
			param.setOut_trade_no(order.getOrderNo());
			param.setMch_id(payConfig.getMchID());
			param.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
			param.setTotal_fee((new Double(order.getActualPayment()*100)).intValue());
			param.setSpbill_create_ip("211.149.240.62");
			param.setNotify_url("http://www.zmz365.com/dreamer/pay/wx/notify.html");
			param.setSign(Signature.getSign(param, payConfig.getKey()));
			String unifiedRes=HttpClient.httpPostForString(UNIFIED_ORDER_URL, param.toXmlString());
			unifiedRes=new String(unifiedRes.getBytes("ISO-8859-1"),"UTF-8");
			UnifiedOrderResData unifiedOrder=Util.getObjectFromXML(unifiedRes, UnifiedOrderResData.class);
			if(unifiedOrder.getReturn_code().equals("FAIL")){
				LOG.error("统一下单调用失败 {}",unifiedOrder.getReturn_msg());
				model.addAttribute("errorMsg", unifiedRes);
				return "pay/pay_error";
			}
			HashMap<String,Object> jsapiParam=new HashMap<String,Object>();
			jsapiParam.put("appId", payConfig.getAppID());
			jsapiParam.put("timeStamp", String.valueOf(System.currentTimeMillis()/1000));
			jsapiParam.put("nonceStr", RandomStringGenerator.getRandomStringByLength(32));
			jsapiParam.put("package", "prepay_id="+unifiedOrder.getPrepay_id());
			jsapiParam.put("signType", "MD5");
			jsapiParam.put("paySign", Signature.getSign(jsapiParam, payConfig.getKey()));
			String jsonParam=JsonUtil.mapToJsonStr(jsapiParam);
			LOG.debug("JSAPI Param：{}",jsonParam);
			model.addAttribute("jsapiParam", jsapiParam);
			model.addAttribute("jsapiParamJson",jsonParam);
			
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("APP支付获取预支付码异常",exp);
		}
		return "pay/pay_index";
	}

	@Autowired
	private PayConfig payConfig;

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private AgentDAO agentDAO;

	private static final String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
			+ "appid={APPID}&secret={SECRET}&code={CODE}&grant_type=authorization_code";

	private final static String UNIFIED_ORDER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
