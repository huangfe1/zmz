package com.dreamer.view.pay;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.pmall.order.PaymentWay;
import com.dreamer.domain.user.Agent;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.pay.*;
import com.dreamer.service.pay.param.PayNoticeData;
import com.dreamer.service.pay.param.UnifiedOrderResData;
import com.dreamer.service.pmall.order.OrderPayHandler;
import com.dreamer.service.user.AgentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.date.DateUtil;
import ps.mx.otter.utils.json.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

@Controller
@RequestMapping("/pay")
public class WXPayController {

	@RequestMapping(value="/wx/dmz/notify.html",method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> payNotify(@RequestBody String body,HttpServletRequest request, HttpServletResponse response){
		LOG.info("订单支付通知 {}",body);
		try{
			PayNoticeData resData=Util.getObjectFromXML(body,PayNoticeData.class);
			if(resData.getReturn_code().toUpperCase().equals("SUCCESS")){
				String orderNo=resData.getOut_trade_no();
				LOG.info("订单支付通知处理 订单号：{} 支付金额:{}",orderNo,resData.getCash_fee());
				Order order=orderDAO.findNoPaidOrderByOrderNo(orderNo);
				if(Objects.isNull(order)){
					LOG.info("订单支付已处理或订单号{}对应的订单不存在",orderNo);
					throw new ApplicationException("订单支付已处理或订单号"+orderNo+"对应的订单不存在");
				}
				LOG.info("订单支付通知处理 订单号：{} 支付金额:{}",orderNo,resData.getCash_fee());
				order.setPaymentTime(DateUtil.string2date(resData.getTime_end(), "yyyyMMddHHmmss"));
				orderPayHandler.pay(order, PaymentWay.WX, new Double(resData.getCash_fee()/100));
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("Pay notice error",exp);
		}
		String ok="<xml><return_code><![CDATA[SUCCESS]]></return_code>  <return_msg><![CDATA[OK]]></return_msg></xml>";
		return new ResponseEntity<String>(ok, HttpStatus.OK);
	}

	/**
	 * 通过Code获取微信id
	 * @param code
	 * @param orderId
	 * @param state
	 * @param request
	 * @param response
     * @param model
     * @return
     */
	@RequestMapping(value = "/callback.html")
	public String callbackPay(
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value="order",required=false) Integer orderId,
			@RequestParam("state") String state, HttpServletRequest request,
			HttpServletResponse response,Model model) {
		try {
			if(Objects.isNull(code)){
				LOG.error("统一下单调用失败 {}","授权获取用户信息失败");
				model.addAttribute("errorMsg", "授权获取用户信息失败");
				return "pay/pay_error";
			}
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());
			Order order=orderDAO.findById(orderId);
			model.addAttribute("order", order);
			if(order.isPaid()){
				LOG.error("统一下单调用失败,订单已支付");
				model.addAttribute("errorMsg", "统一下单支付失败,订单已支付完成");
				return "pay/pay_error";
			}
			HashMap<String,String> map=getOpenIdHandler.getOpenId(payConfig, code);
			String openid = map.get("openid");
			LOG.debug("获取到用户openid:{}", openid);
			if(Objects.nonNull(openid)) {//获取的openid不为空
				agent.setWxOpenid(openid);
				agentHandler.setWxOpenIdTo(agent, openid);
			}
			UnifiedOrderResData unifiedOrder=unifiedOrderHandler.unifiedOrder(payConfig,agent, order, "和之初积分商城订单");
			if(unifiedOrder.getReturn_code().equals("FAIL")){
				LOG.error("统一下单调用失败 {}",unifiedOrder.getReturn_msg());
				model.addAttribute("errorMsg", "统一下单支付失败,请稍后重试"+unifiedOrder.getReturn_msg());
				return "pay/pay_error";
			}
			String jsonParam=JsonUtil.mapToJsonStr(jsApiParameterFactory.build(payConfig, unifiedOrder.getPrepay_id()));
			LOG.debug("JSAPI Param：{}",jsonParam);
			model.addAttribute("jsapiParamJson",jsonParam);
			
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("APP支付获取预支付码异常",exp);
		}
		return "pay/pay_index";
	}

	@RequestMapping(value="/pay.html",method=RequestMethod.GET)
	public String pay(@RequestParam(value="order",required=false) Integer orderId,
			HttpServletRequest request,
			HttpServletResponse response,Model model){
		try{
			Agent agentUser = (Agent) WebUtil.getCurrentUser(request);
			Agent agent = agentDAO.findById(agentUser.getId());			
			Order order=orderDAO.findById(orderId);
			model.addAttribute("order", order);
			if(order.isPaid()){
				LOG.error("统一下单调用失败,订单已支付");
				model.addAttribute("errorMsg", "统一下单支付失败,订单已支付完成");
				return "pay/pay_error";
			}
			UnifiedOrderResData unifiedOrder=unifiedOrderHandler.unifiedOrder(payConfig,agent, order, "和之初积分商城订单");
			if(unifiedOrder.getReturn_code().equals("FAIL")){
				LOG.error("统一下单调用失败 {}",unifiedOrder.getReturn_msg());
				model.addAttribute("errorMsg", "统一下单支付失败,请稍后重试");
				return "pay/pay_error";
			}
			String jsonParam=JsonUtil.mapToJsonStr(jsApiParameterFactory.build(payConfig, unifiedOrder.getPrepay_id()));
			LOG.debug("JSAPI Param：{}",jsonParam);
			model.addAttribute("jsapiParamJson",jsonParam);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return "pay/pay_index";
	}


	@Autowired
	private PayConfig payConfig;

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private AgentDAO agentDAO;
	
	@Autowired
	private AgentHandler agentHandler;
	
	@Autowired
	private GetOpenIdHandler getOpenIdHandler;
	
	@Autowired
	private JsApiParameterFactory jsApiParameterFactory;
	@Autowired
	private UnifiedOrderHandler unifiedOrderHandler;
	@Autowired
	private OrderPayHandler orderPayHandler;

	/*private static final String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
			+ "appid={APPID}&secret={SECRET}&code={CODE}&grant_type=authorization_code";

	private final static String UNIFIED_ORDER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
	*/
	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
