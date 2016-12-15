package com.dreamer.view.user;

import com.dreamer.domain.pmall.order.OrderItem;
import com.dreamer.domain.pmall.order.PaymentStatus;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.domain.user.VoucherTransfer;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.pmall.order.OrderItemDao;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.repository.user.UserDAO;
import com.dreamer.repository.user.VoucherTransferDAO;
import com.dreamer.service.pmall.order.OrderPayHandler;
import com.dreamer.service.user.AgentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/voucher")
public class VoucherTransferController {
    /**
     * 返利模式是5-1-1
     * @return
     */
	@RequestMapping(value = "/backvoucher")
	public String  getVoucherBack(){
        int j=0;
        try {
        List<OrderItem> orderItems = orderDAO.getOrdersMoreThan1();//找出所有的订单
       for (OrderItem item : orderItems){
           if(item.getOrder().getPaymentStatus()!= PaymentStatus.PAID)continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz111668"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz684560"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz207261"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz935790"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz357540"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz102855"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz102855"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz973212"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz236125"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz630868"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz516092"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz122959"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz796585"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz854924"))continue;
           if(item.getOrder().getUser().getParent().getAgentCode().contains("zmz812650"))continue;
           j++;

           Agent a = item.getOrder().getUser();//下单人
           Integer[] vouchers ={5,1,1};
           if (j > 256) {
               System.out.println("下单人:"+item.getOrder().getUser().getRealName()+"下单数"+item.getQuantity());
           for(int i=0 ;i<vouchers.length;i++ ) {//开始循环

                   a = a.getParent();
                   double backvoucher = item.getQuantity() * item.getQuantity() * vouchers[i] - vouchers[i] * 1;
                   System.out.println("上家" + i + a.getRealName() + "需要返回" + backvoucher);
                   String more = "下家购买胸膜体验装" + item.getQuantity() + "片,代金券追回";
                   Agent admin = mutedUserDAO.loadFirstOne();
                   a.getAccounts().deductVoucherForBack(backvoucher, more);
                   orderPayHandler.back(item);
                   if (a.isMutedUser()) break;//如果是上家则退出
               }
           }
       }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(j);
        return "SUCCESS";
	}



	@RequestMapping(value = "/transfer.json", method = RequestMethod.POST)
	public Message transfer(
			@ModelAttribute("parameter") VoucherTransfer transfer,
			@RequestParam("realName") String realName,
			@RequestParam("agentCode") String agentCode,
			@RequestParam(value = "agentFromrealName",required = false) String agentFromrealName,
			@RequestParam(value = "agentFromCode",required = false) String agentFromCode,
			Model model,
			HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAgent()) {
				Agent agent = agentDAO.findById(user.getId());
				transfer.setUserByFromAgent(agent);
				agentHandler.transferVoucher(user, transfer, agentCode,
						realName, transfer.getVoucher());
			}
			if (user.isAdmin()) {
//				Agent agent = mutedUserDAO.loadFirstOne();
//				transfer.setUserByFromAgent(agent);
				//这里修改一下,转出人由管理员自己设定
				Agent agent= (Agent) userDAO.findByAgentCode(agentFromCode.trim()).get(0);
				transfer.setUserByFromAgent(agent);

				agentHandler.transferVoucher(user, transfer, agentCode,
						realName, transfer.getVoucher());
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/remove.json")
	public Message remove(
			@ModelAttribute("parameter") VoucherTransfer voucherTransfer,HttpServletRequest request) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				agentHandler.removeVoucher(user, voucherTransfer);
			}else if(user.isAgent()){
				return Message.createFailedMessage("本操作仅限于管理员权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}




	@ModelAttribute("parameter")
	public VoucherTransfer preprocess(@RequestParam("id") Optional<Integer> id) {
		VoucherTransfer parameter;
		if (id.isPresent()) {
			parameter = voucherTransferDAO.findById(id.get());
		} else {
			parameter = new VoucherTransfer();
		}
		return parameter;
	}
    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderItemDao orderItem;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private VoucherTransferDAO voucherTransferDAO;
	@Autowired
	private AgentHandler agentHandler;
    @Autowired
    private OrderPayHandler orderPayHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
