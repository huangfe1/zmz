package com.dreamer.view.pay;

import com.dreamer.domain.user.*;
import com.dreamer.repository.user.AccountsDAO;
import com.dreamer.repository.user.AdvanceTransferDAO;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Controller
@RequestMapping("/advance/pay")
public class AdvanceWXPayController {
    @Autowired
    private PayConfig payConfig;
    @Autowired
    private GetOpenIdHandler getOpenIdHandler;
    @Autowired
    private AdvanceTransferDAO advanceTransferDAO;
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
    @Autowired
    private AccountsDAO accountsDAO;


    @RequestMapping("/index.html")
    public String index(HttpServletRequest request, Model model) {
        User user = (User) WebUtil.getCurrentUser(request);
        Accounts acs = accountsDAO.findByAgentId(user.getId());
        model.addAttribute("accounts", acs);
        return "/user/advance_pay";
    }

    @RequestMapping("/pay.html")
    public String pay(@RequestParam(required = false) String code, Integer orderId, Model model, HttpServletRequest request) {
        try {
            AdvanceTransfer advanceTransfer = advanceTransferDAO.findById(orderId);
            model.addAttribute("order", advanceTransfer);
            if (advanceTransfer.getType() == AdvanceTransferType.PAY) {//已经支付
                model.addAttribute("errorMsg", "已经支付的订单");
                return "pay/advance_pay_error";
            }
            Agent agent = (Agent) WebUtil.getCurrentUser(request);
            agent = agentDAO.findById(agent.getId());//session中的已经过时
            //if (Objects.isNull(agent.getWxOpenid()) || agent.getWxOpenid().isEmpty()) {//没有openid
            if (Objects.isNull(code)) {
                model.addAttribute("errorMsg", "获取授权失败");
                return "pay/advance_pay_error";
            }
            HashMap<String, String> map = getOpenIdHandler.getOpenId(payConfig, code);
            String openId = map.get("openid");
            agent.setWxOpenid(openId);//设置微信ID
            agentHandler.setWxOpenIdTo(agent, openId);
            //}
            //String notifyUrl = ServletUriComponentsBuilder.fromContextPath(request).path("/voucher/pay/notify.html").build().toUriString();
            String notifyUrl = "http://www.zmz365.com/dreamer/advance/pay/dmz/notify.html";
            UnifiedOrderResData unifiedOrder = unifiedOrderHandler.unifiedOrder(notifyUrl, advanceTransfer.getOut_trade_no(), advanceTransfer.getAdvance() - advanceTransfer.getUseVoucher(), payConfig, agent, "和之初生物科技预存款充值");

            if (unifiedOrder.getReturn_code().equals("FAIL")) {
                model.addAttribute("errorMsg", "统一下单支付失败,请稍后重试");
                return "pay/advance_pay_error";
            }
            String jsonParam = JsonUtil.mapToJsonStr(jsApiParameterFactory.build(payConfig, unifiedOrder.getPrepay_id()));
            model.addAttribute("jsapiParamJson", jsonParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pay/pay_advance";

    }

    @RequestMapping("/alipay.html")
    public String pay(Integer orderId, Model model, HttpServletRequest request) {
        try {
            AdvanceTransfer advanceTransfer = advanceTransferDAO.findById(orderId);
            model.addAttribute("order", advanceTransfer);
            if (advanceTransfer.getType() == AdvanceTransferType.PAY) {//已经支付
                model.addAttribute("errorMsg", "已经支付的订单");
                return "pay/advance_pay_error";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pay/alipay_advance";

    }

    @RequestMapping(value = "/dmz/notify.html", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> notify(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) {
        try {
            PayNoticeData resData = Util.getObjectFromXML(body, PayNoticeData.class);
            if (resData.getResult_code().toUpperCase().equals("SUCCESS")) {
                String orderNo = resData.getOut_trade_no();
                AdvanceTransfer temp = new AdvanceTransfer();
                temp.setOut_trade_no(orderNo);
                AdvanceTransfer advanceTransfer = advanceTransferDAO.findByExample(temp).get(0);//获取第一个
                if (advanceTransfer.getType() == AdvanceTransferType.PAY) {
                    throw new ApplicationException("订单" + orderNo + "已经支付,无需再次支付");
                }
                agentHandler.payForAdvance(resData.getTime_end(), advanceTransfer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ok = "<xml><return_code><![CDATA[SUCCESS]]></return_code>  <return_msg><![CDATA[OK]]></return_msg></xml>";
        return new ResponseEntity<String>(ok, HttpStatus.OK);
    }

    /**
     * 支付宝通知
     *
     * @param agentCode
     * @param money
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/dmz/aliNotify.html", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> aliNotify(String agentCode, Double money, Integer orderId, HttpServletRequest request, HttpServletResponse response) {
        VoucherTransfer voucherTransfer = new VoucherTransfer();
//        AdvanceTransfer advanceTransfer=new AdvanceTransfer();

        if (orderId != null && orderId != 0) {//orderId存在
            voucherTransfer = voucherTransferDAO.findById(orderId);//找出最近的一条订单
        } else {
            agentCode = agentCode.toLowerCase();//最小化
            Agent agent = agentDAO.findByAgentCode(agentCode);
            if (!Objects.isNull(agent)) {//如果存在这个代理
                voucherTransfer = voucherTransferDAO.findByAgentId(agent.getId());//找出最近的一条订单
//                advanceTransfer = advanceTransferDAO.findByAgentId(agent.getId());//找出最近的一条订单
            }
        }

        if (voucherTransfer.getType() == VoucherTransferType.PAY)//如果已经支付
            return new ResponseEntity<String>("error/has pay", HttpStatus.OK);
//            if(voucherTransfer.getVoucher()>money){//支付的金额不够
//				voucherTransfer.setType(VoucherTransferType.ERROR);
//				voucherTransfer.setRemark("支付了"+money+"元");
//					agentHandler.mergeVoucher(voucherTransfer);
//					return new ResponseEntity<String>("error/not enough", HttpStatus.OK);
//                }else {
//                    agentHandler.payForVoucher(voucherTransfer.getUpdateTime().toString(),voucherTransfer);
//                }
        if (voucherTransfer.getVoucher().equals(money)) {//只有当下单的金钱等于充值的金钱才能成功
            agentHandler.payForVoucher(voucherTransfer.getUpdateTime().toString(), voucherTransfer);
        }
        String ok = "<xml><return_code><![CDATA[SUCCESS]]></return_code>  <return_msg><![CDATA[OK]]></return_msg></xml>";
        return new ResponseEntity<String>(ok, HttpStatus.OK);

    }

    public static void main(String[] args) {
        Double m=0.1;
        Double y=0.20;
        System.out.println(m.equals(y));
    }


    @RequestMapping(value = "/dmz/paybyvoucher.html")
    public String payByVoucher(Integer transferId, Model model) {
        try {
            SimpleDateFormat sd = new SimpleDateFormat("YYYY-mm-dd HH:MM:SS");
            AdvanceTransfer advanceTransfer = advanceTransferDAO.findById(transferId);
            agentHandler.payForAdvanceByVoucher(sd.format(new Date()), advanceTransfer);
        } catch (Exception e) {
            //e.printStackTrace();
            model.addAttribute("errorMsg", e.getMessage());
            return "pay/pay_advanceByVoucher_error";
        }
        return "pay/pay_advanceByVoucher_success";
    }

}
