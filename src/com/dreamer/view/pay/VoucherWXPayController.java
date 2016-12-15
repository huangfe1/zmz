package com.dreamer.view.pay;

import com.dreamer.domain.user.*;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.VoucherTransferDAO;
import com.dreamer.service.pay.*;
import com.dreamer.service.pay.param.PayNoticeData;
import com.dreamer.service.pay.param.UnifiedOrderResData;
import com.dreamer.service.user.AgentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.json.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Objects;

@Controller
@RequestMapping("/voucher/pay")
public class VoucherWXPayController{
	@Autowired
	private PayConfig payConfig;
	@Autowired
	private GetOpenIdHandler getOpenIdHandler;
	@Autowired
	private VoucherTransferDAO voucherTransferDAO;
	@Autowired
	private AgentHandler agentHandler;
	@Autowired
	private UnifiedOrderHandler unifiedOrderHandler;
	@Autowired
	private JsApiParameterFactory jsApiParameterFactory;
    @Autowired
    private AgentDAO agentDAO;
	@RequestMapping("/index.html")
	public String index(){
		return "/user/voucher_pay";
	}

	@RequestMapping("/pay.html")
	public String pay(@RequestParam(required = false)String code,Integer orderId,Model model,HttpServletRequest request){
		try {
			VoucherTransfer voucherTransfer = voucherTransferDAO.findById(orderId);
			model.addAttribute("order", voucherTransfer);
			if (voucherTransfer.getType() == VoucherTransferType.PAY) {//已经支付
				model.addAttribute("errorMsg", "已经支付的订单");
				return "pay/voucher_pay_error";
			}
			Agent agent = (Agent) WebUtil.getCurrentUser(request);
            agent=agentDAO.findById(agent.getId());//session中的已经过时
			//if (Objects.isNull(agent.getWxOpenid()) || agent.getWxOpenid().isEmpty()) {//没有openid
				if (Objects.isNull(code)) {
					model.addAttribute("errorMsg", "获取授权失败");
					return "pay/voucher_pay_error";
				}
				HashMap<String, String> map = getOpenIdHandler.getOpenId(payConfig, code);
				String openId = map.get("openid");
				agent.setWxOpenid(openId);//设置微信ID
				agentHandler.setWxOpenIdTo(agent, openId);
			//}
			//String notifyUrl = ServletUriComponentsBuilder.fromContextPath(request).path("/voucher/pay/notify.html").build().toUriString();
            String notifyUrl ="http://www.zmz365.com/dreamer/voucher/pay/dmz/notify.html";
            UnifiedOrderResData unifiedOrder = unifiedOrderHandler.unifiedOrder(notifyUrl, voucherTransfer.getOut_trade_no(), voucherTransfer.getVoucher(), payConfig, agent, "和之初生物科技代金券充值");

			if (unifiedOrder.getReturn_code().equals("FAIL")) {
				model.addAttribute("errorMsg", "统一下单支付失败,请稍后重试");
				return "pay/voucher_pay_error";
			}
			String jsonParam = JsonUtil.mapToJsonStr(jsApiParameterFactory.build(payConfig, unifiedOrder.getPrepay_id()));
			model.addAttribute("jsapiParamJson", jsonParam);
		}catch (Exception e){
			e.printStackTrace();
		}
		return "/pay/pay_voucher";

	}
	@RequestMapping("/alipay.html")
	public String pay(Integer orderId,Model model,HttpServletRequest request){
		try {
			VoucherTransfer voucherTransfer = voucherTransferDAO.findById(orderId);
			model.addAttribute("order", voucherTransfer);
			if (voucherTransfer.getType() == VoucherTransferType.PAY) {//已经支付
				model.addAttribute("errorMsg", "已经支付的订单");
				return "pay/voucher_pay_error";
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "/pay/alipay_voucher";

	}


	@RequestMapping(value = "/dmz/notify.html",method= RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> notify(@RequestBody String body, HttpServletRequest request, HttpServletResponse response){
        try {
			PayNoticeData resData= Util.getObjectFromXML(body,PayNoticeData.class);
			if(resData.getResult_code().toUpperCase().equals("SUCCESS")){
				String orderNo = resData.getOut_trade_no();
				VoucherTransfer temp = new VoucherTransfer();
				temp.setOut_trade_no(orderNo);
				VoucherTransfer voucherTransfer=voucherTransferDAO.findByExample(temp).get(0);//获取第一个
				if(voucherTransfer.getType()==VoucherTransferType.PAY){
					throw  new ApplicationException("订单"+orderNo+"已经支付,无需再次支付");
				}

				agentHandler.payForVoucher(resData.getTime_end(),voucherTransfer);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		String ok="<xml><return_code><![CDATA[SUCCESS]]></return_code>  <return_msg><![CDATA[OK]]></return_msg></xml>";
		return new ResponseEntity<String>(ok, HttpStatus.OK);
	}

}
