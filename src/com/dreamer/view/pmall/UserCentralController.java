package com.dreamer.view.pmall;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.pmall.order.Order;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.pmall.order.OrderDAO;
import com.dreamer.repository.user.AgentDAO;

@Controller
@RequestMapping(value="/pmall/uc")
public class UserCentralController {
	
	@RequestMapping(value="/index.html",method=RequestMethod.GET)
	public String index(@ModelAttribute("agent") Agent agent,HttpServletRequest request,Model model){
		try{
			List<Order> orders=orderDAO.findLatelyThreeMonthOrdersOfAgent(agent);
			model.addAttribute("orders", orders);
			//黄飞增加查看代理的积分商城订单
			List<Order> other_orders=orderDAO.findLatelyThreeMonthOrdersOfCustom(agent);
			model.addAttribute("other_orders", other_orders);
			
			
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return "pmall/uc/uc_index";
	}
	
	@ModelAttribute("agent")
	public Agent preprocess(@RequestParam("id") Optional<Integer> id,HttpServletRequest request){
		if(id.isPresent()){
			return agentDAO.findById(id.get());
		}else{
			User user=(User)WebUtil.getCurrentUser(request);
			return agentDAO.findById(user.getId());
		}
	}
	
	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private AgentDAO agentDAO;


}
