package com.dreamer.service.pay.param;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.dreamer.service.pay.PayConfig;
import com.dreamer.service.pay.RandomStringGenerator;
import com.dreamer.service.pay.Signature;

public class PayUnifiedOrderReqData {

	private String appid = "";
	private String mch_id = "";
	private String device_info = "";
	private String nonce_str = "";
	private String sign = "";
	private String body = "";
	private String detail = "";
	private String attach = "";
	private String out_trade_no = "";
	private Integer total_fee = 0;
	private String fee_type = "";
	private String spbill_create_ip = "";
	private String time_start = "";
	private String time_expire = "";
	private String goods_tag = "";
	private String notify_url="";
	private String trade_type="JSAPI";
	private String product_id="";
	private String limit_pay="";
	private String openid="";

	public String toXmlString() {
		StringBuffer str = new StringBuffer();

		str.append("<xml>").append("<appid>").append(getAppid())
				.append("</appid>");
		if (Objects.nonNull(getAttach()) && !getAttach().isEmpty()) {
			str.append("<attach>").append(getAttach()).append("</attach>");
		}
		str.append("<body>").append(getBody()).append("</body>");
		if (Objects.nonNull(getDetail()) && !getDetail().isEmpty()) {
			str.append("<detail>").append(detail).append("</detail>");
		}
		if (Objects.nonNull(getDevice_info()) && !getDevice_info().isEmpty()) {
			str.append("<device_info>").append(getDevice_info())
					.append("</device_info");
		}
		if (Objects.nonNull(getGoods_tag()) && !getGoods_tag().isEmpty()) {
			str.append("<goods_tag>").append(getGoods_tag())
					.append("</goods_tag>");
		}
		str.append("<mch_id>").append(getMch_id()).append("</mch_id>");
		str.append("<nonce_str>").append(getNonce_str()).append("</nonce_str>");
		str.append("<notify_url>").append(getNotify_url()).append("</notify_url>");
		if(Objects.nonNull(getOpenid()) && !getOpenid().isEmpty()){
			str.append("<openid>").append(getOpenid()).append("</openid>");
		}
		str.append("<out_trade_no>").append(getOut_trade_no())
				.append("</out_trade_no>");
		str.append("<spbill_create_ip>").append(getSpbill_create_ip())
				.append("</spbill_create_ip>");
		if (Objects.nonNull(getTime_expire()) && !getTime_expire().isEmpty()) {
			str.append("<time_expire>").append(getTime_expire())
					.append("</time_expire>");
		}
		if (Objects.nonNull(getTime_start()) && !getTime_start().isEmpty()) {
			str.append("<time_start>").append(getTime_start())
					.append("</time_start>");
		}
		str.append("<total_fee>").append(getTotal_fee()).append("</total_fee>");
		str.append("<trade_type>").append(getTrade_type()).append("</trade_type>");
		str.append("<sign>").append(getSign()).append("</sign>");
		str.append("</xml>");
		return str.toString();
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object obj;
			try {
				obj = field.get(this);
				if (obj != null) {
					map.put(field.getName(), obj);
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public PayUnifiedOrderReqData() {
	}

	public PayUnifiedOrderReqData(PayConfig config,String body,
			String attach, String outTradeNo, int totalFee, String deviceInfo,
			String spBillCreateIP, String timeStart, String timeExpire,
			String goodsTag) {
		setAppid(config.getAppID());
		setMch_id(config.getMchID());
		setBody(body);
		setAttach(attach);
		setOut_trade_no(outTradeNo);
		setTotal_fee(totalFee);
		setDevice_info(deviceInfo);
		setSpbill_create_ip(spBillCreateIP);
		setTime_start(timeStart);
		setTime_expire(timeExpire);
		setGoods_tag(goodsTag);
		setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
		String sign = Signature.getSign(toMap(), config.getKey());
		setSign(sign);
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}

	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	
}
