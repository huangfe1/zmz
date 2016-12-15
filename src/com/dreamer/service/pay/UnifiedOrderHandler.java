package com.dreamer.service.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.http.HttpClient;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.user.Agent;
import com.dreamer.service.pay.param.PayUnifiedOrderReqData;
import com.dreamer.service.pay.param.UnifiedOrderResData;

@Service
public class UnifiedOrderHandler {

	public UnifiedOrderResData unifiedOrder(PayConfig payConfig,Agent agent, Order order,String body) {
		try {
			PayUnifiedOrderReqData param = new PayUnifiedOrderReqData();
			param.setAppid(payConfig.getAppID());
			param.setBody(body);
			param.setOpenid(agent.getWxOpenid());
//			System.out.println("========="+agent.getWxOpenid());
			param.setOut_trade_no(order.getOrderNo());
			param.setMch_id(payConfig.getMchID());
			param.setNonce_str(RandomStringGenerator
					.getRandomStringByLength(32));
			param.setTotal_fee((new Double(order.getActualPayment() * 100))
					.intValue());
			param.setSpbill_create_ip(payConfig.getSpbill_create_ip());
			param.setNotify_url(payConfig.getNotifyUrl());
			param.setSign(Signature.getSign(param, payConfig.getKey()));
			String unifiedRes = HttpClient.httpPostForString(UNIFIED_ORDER_URL,
					param.toXmlString());
			unifiedRes = new String(unifiedRes.getBytes("ISO-8859-1"), "UTF-8");
			UnifiedOrderResData unifiedOrder = Util.getObjectFromXML(
					unifiedRes, UnifiedOrderResData.class);
			return unifiedOrder;
		} catch (Exception exp) {
			LOG.error("统一下单调用失败",exp);
			exp.printStackTrace();
			throw new ApplicationException("统一下单调用异常",exp);
		}
	}

	public UnifiedOrderResData unifiedOrder(String notifyUrl,String orderNo,Double number,PayConfig payConfig,Agent agent,String body){
		try {
			PayUnifiedOrderReqData param = new PayUnifiedOrderReqData();
			param.setAppid(payConfig.getAppID());
			param.setBody(body);
			param.setOpenid(agent.getWxOpenid());
			param.setOut_trade_no(orderNo);
			param.setMch_id(payConfig.getMchID());
			param.setNonce_str(RandomStringGenerator
					.getRandomStringByLength(32));
			param.setTotal_fee((new Double(number * 100))
					.intValue());
			param.setSpbill_create_ip(payConfig.getSpbill_create_ip());
			param.setNotify_url(notifyUrl);
			param.setSign(Signature.getSign(param, payConfig.getKey()));
			String unifiedRes = HttpClient.httpPostForString(UNIFIED_ORDER_URL,
					param.toXmlString());
			unifiedRes = new String(unifiedRes.getBytes("ISO-8859-1"), "UTF-8");
			UnifiedOrderResData unifiedOrder = Util.getObjectFromXML(
					unifiedRes, UnifiedOrderResData.class);
			return unifiedOrder;
		} catch (Exception exp) {
			LOG.error("统一下单调用失败",exp);
			exp.printStackTrace();
			throw new ApplicationException("统一下单调用异常",exp);
		}
	}

	private final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	private final Logger LOG = LoggerFactory.getLogger(getClass());
}
