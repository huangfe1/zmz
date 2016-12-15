package com.dreamer.view.user;

import com.dreamer.domain.user.AdvanceTransfer;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AdvanceTransferDAO;
import com.dreamer.service.pay.GetOpenIdHandler;
import com.dreamer.service.pay.PayConfig;
import com.dreamer.service.user.AgentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/advance/pay")
public class AdvancePayController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
	@RequestMapping(value = "/commit.json",method = RequestMethod.POST)
	public Message commit( AdvanceTransfer advanceTransfer , Boolean isUseVoucher){
        try {
            User user=(User)WebUtil.getCurrentUser(request);
            Agent agent = agentHandler.findAgentById(user.getId());
            Double agent_voucher=agent.getAccounts().getVoucherBalance();
            if(Objects.nonNull(isUseVoucher)&&isUseVoucher){//如果使用代金券
                advanceTransfer.setUseVoucher(agent_voucher<advanceTransfer.getAdvance()?agent_voucher:advanceTransfer.getAdvance());
            }else {
                advanceTransfer.setUseVoucher(0.0);//不使用代金券
            }
            agentHandler.addAdvance(user,advanceTransfer);//提交充值预存款定订单
                //代金券充足  直接提交
                if(advanceTransfer.getUseVoucher()==advanceTransfer.getAdvance()){//代金券充足
                    String uri= ServletUriComponentsBuilder.fromContextPath(request).path("/advance/pay/dmz/paybyvoucher.html").queryParam("transferId",advanceTransfer.getId()).build().toUriString();
                    response.setHeader("Location",uri);
                    return Message.createSuccessMessage("正在充值..请稍等..");
                }
            String uri;
            String backUrl= ServletUriComponentsBuilder.fromContextPath(request).path("/advance/pay/pay.html").queryParam("orderId",advanceTransfer.getId()).build().toUriString();
//            if(Objects.nonNull(user.getWxOpenid())&&!user.getWeixin().isEmpty()){//有OpenID直接去下单
//                uri=backUrl;
//            }else{//先去获得网页授权,返回Code
                uri= GetOpenIdHandler.createGetBaseOpenIdCallbackUrl(payConfig, backUrl, advanceTransfer.getId());
//            }
            response.setHeader("Location",uri);
            return Message.createSuccessMessage("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return Message.createFailedMessage(e.getMessage());
        }
	}

    @RequestMapping(value = "/alicommit.json",method = RequestMethod.POST)
    public Message alicommit( AdvanceTransfer advanceTransfer ){
        try {
            User user=(User)WebUtil.getCurrentUser(request);
            advanceTransfer.setUseVoucher(0.0);//不使用代金券
            agentHandler.addAdvance(user,advanceTransfer);//提交充值预存款定订单
            String backUrl= ServletUriComponentsBuilder.fromContextPath(request).path("/advance/pay/alipay.html").queryParam("orderId",advanceTransfer.getId()).build().toUriString();
            response.setHeader("Location",backUrl);
            return Message.createSuccessMessage("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return Message.createFailedMessage(e.getMessage());
        }
    }

//    @ModelAttribute("parameter")
//    public VoucherTransfer preprocess(@RequestParam("id")Optional<Integer> id){
//        if(id.isPresent()){
//
//        }else {
//           // return new VoucherTransfer();
//        }
//
//    }

    @Autowired
    private AgentHandler agentHandler;
    @Autowired
    private PayConfig payConfig;
    @Autowired
    private AdvanceTransferDAO advanceTransferDAO;

}
