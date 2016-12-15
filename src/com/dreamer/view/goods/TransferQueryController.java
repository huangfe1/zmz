package com.dreamer.view.goods;

import com.dreamer.domain.goods.GoodsTransferStatus;
import com.dreamer.domain.goods.Transfer;
import com.dreamer.domain.user.Accounts;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.MutedUser;
import com.dreamer.domain.user.User;
import com.dreamer.repository.account.GoodsAccountDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.goods.TransferDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.service.goods.TransferHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transfer")
public class TransferQueryController {

//	@RequestMapping(value = { "/my.html", "/search.html" }, method = RequestMethod.GET)
//	public String index(
//			@ModelAttribute("parameter") SearchParameter<Goods> parameter,
//			@RequestParam(value = "from", required = false) Integer from,
//			HttpServletRequest request, Model model) {
//		try {
//			User user = (User) WebUtil.getCurrentUser(request);
//			if (user.isAdmin()) {
//				List<GoodsAccountDTO> dtos = new ArrayList<GoodsAccountDTO>();
//				List<Goods> goods = goodsDAO.searchEntityByPage(parameter,
//						null, (t) -> true);
//				WebUtil.turnPage(parameter, request);
//				goods.forEach((g) -> {
//					GoodsAccountDTO d = new GoodsAccountDTO();
//					d.setCurrentBalance(g.getCurrentBalance());
//					d.setCumulative(0);
//					d.setCurrentPoint(g.getCurrentPoint());
//					d.setStatus(AgentStatus.ACTIVE);
//					d.setPrice(0.0);
//					d.setGoods(g);
//					dtos.add(d);
//				});
//				model.addAttribute("goods", dtos);
//			} else {
//				Agent agent = agentDAO.findById(user.getId());
//				List<GoodsAccountDTO> dtos = new ArrayList<GoodsAccountDTO>();
//				Set<Authorization> auths = agent.getAuthorizations();
//				auths.forEach((g) -> {
//					Goods goods = g.getAuthorizedGoods();
//					String searchName = parameter.getEntity().getName();
//					if (searchName != null && !searchName.trim().isEmpty()) {
//						if (goods.getName().indexOf(searchName) < 0) {
//							return;
//						}
//					}
//					GoodsAccountDTO d = new GoodsAccountDTO();
//					GoodsAccount acs = agent.loadAccountForGoodsNotNull(goods);
//					if (acs != null) {
//						d.setCumulative(acs.getCumulative());
//						d.setCurrentBalance(acs.getCurrentBalance());
//						d.setCurrentPoint(acs.getCurrentPoint());
//						d.setPrice(acs.getPrice());
//					} else {
//						d.setCumulative(0);
//						d.setCurrentBalance(0);
//						d.setCurrentPoint(0D);
//						d.setPrice(0.0);
//					}
//					d.setGoods(g.getAuthorizedGoods());
//					d.setStatus(g.getStatus());
//					dtos.add(d);
//				});
//				model.addAttribute("goods", dtos);
//				model.addAttribute("from", user.getId());
//				model.addAttribute("teqVip",agent.isTeqVip());
//                System.out.println(agent.isTeqVip()+"-----");
//            }
//		} catch (Exception exp) {
//			exp.printStackTrace();
//			LOG.error("可转货产品查询失败", exp);
//		}
//		return "/goods/transfer_index";
//	}



    @RequestMapping(value = { "/my.html", "/search.html" }, method = RequestMethod.GET)
    public String index(
            HttpServletRequest request, Model model) {
            User user = (User) WebUtil.getCurrentUser(request);
            Agent agent =agentDAO.findById(user.getId());
            model.addAttribute("teqVip",agent.isTeqVip());
        return "/goods/transfer_index";
    }


/**
 * 转货操作的代码
 * @param parameter
 * @param request
 * @param model
 * @return
 */
	@RequestMapping(value = "/from.html", method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("transfer") SearchParameter<Transfer> parameter,
			HttpServletRequest request, Model model) {
		User user = (User) WebUtil.getCurrentUser(request);
		try {
			if (user.isAdmin()) {
			/*	Goods goods = parameter.getEntity();
				model.addAttribute("currentBalance", goods.getCurrentBalance());
				model.addAttribute("price", 0.0D);*/
			} else {
				if (user.isAgent()) {
					Agent agent = agentDAO.findById(user.getId());
				/*	if (!agent.isActivedAuthorizedGoods(parameter.getEntity())) {
						throw new NotAuthorizationException("您对该产品没有授权");
					}
					if (!user.isTopAgent()) {
						Agent parent = (Agent) user.getParent();
						if (!parent.isActivedAuthorizedGoods(parameter
								.getEntity())) {
							throw new NotAuthorizationException(
									"您申请转货的产品对方未获得该产品授权");
						}
						GoodsAccount gac = parent.loadAccountForGoods(parameter
								.getEntity());
						if (gac == null) {
							throw new DataNotFoundException("对方产品账户不存在");
						}
						model.addAttribute("currentBalance",
								gac.getCurrentBalance());
					} else {
						model.addAttribute("currentBalance", parameter
								.getEntity().getCurrentBalance());
					}
					
					GoodsAccount gac = agent.loadAccountForGoods(parameter
							.getEntity());
					if (gac == null) {
						throw new DataNotFoundException("产品账户不存在");
					}*/
					Accounts acs = agent.getAccounts();
					model.addAttribute("voucher", acs.getVoucherBalance());
					/*model.addAttribute("price", gac.getPrice());*/
				}
			}
		} catch (Exception nep) {
			model.addAttribute("message", nep.getMessage());
			return "goods/transfer_403";
		}
		return "goods/transfer_from";

	}

	@RequestMapping(value = "/to.html", method = RequestMethod.GET)
	public String transferTo(
			@ModelAttribute("transfer") SearchParameter<Transfer> parameter,
			@RequestParam(value="back",required=false) Optional<String> back,
			HttpServletRequest request, Model model) {
		try {
//			User user = (User) WebUtil.getCurrentUser(request);
//			if(user.isAdmin()){
//				//throw new ApplicationException("此操作只能代理本用户进行");
//			}
//			Agent agent = agentDAO.findById(user.getId());
			model.addAttribute("toback", 0);
			back.ifPresent(s->{
//				if(agent.isTopAgent()){
//					MutedUser mutedUser=(MutedUser)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY);
//					model.addAttribute("toAgentName",mutedUser.getRealName());
//					model.addAttribute("toAgentCode", mutedUser.getAgentCode());
//				}else{
//					model.addAttribute("toAgentName",agent.getParent().getRealName());
//					model.addAttribute("toAgentCode", ((Agent)agent.getParent()).getAgentCode());
//				}
				model.addAttribute("toback", 1);				
			});
			
			
			/*if (!agent.isActivedAuthorizedGoods(parameter.getEntity())) {
				throw new NotAuthorizationException("您对该产品没有授权");
			}
			GoodsAccount gac = agent.loadAccountForGoods(parameter.getEntity());
			if(gac==null){
				throw new DataNotFoundException("产品账户不存在");
			}
			model.addAttribute("currentBalance", gac.getCurrentBalance());*/
		} catch (Exception exp) {
			exp.printStackTrace();
			model.addAttribute("message", exp.getMessage());
			return "goods/transfer_403";
		}
		return "goods/transfer_to";

	}
	
	@RequestMapping(value = "/records.html", method = RequestMethod.GET)
	public String records(
			@ModelAttribute("transfer") SearchParameter<Transfer> parameter,
			@RequestParam(value="agent",required=false) Integer agentId,
			@RequestParam(required=false) Optional<String> applyAgent,
			HttpServletRequest request, Model model) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);		
			if(applyAgent.isPresent()){
				model.addAttribute("applyAgent", applyAgent.get());
			}
			if(user.isAdmin()){
				List<Transfer> ts=transferDAO.searchEntityByPage(parameter, null, null,null, applyAgent);
				WebUtil.turnPage(parameter, request);
				model.addAttribute("ts", ts);
			}else{
				if(agentId==null){
					agentId=user.getId();
				}
				List<Transfer> ts=transferDAO.searchEntityByPage(parameter, null, null, agentId,applyAgent);
				WebUtil.turnPage(parameter, request);
				model.addAttribute("ts", ts);
			}
			model.addAttribute("from", user.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
			model.addAttribute("message", exp.getMessage());
			return "goods/transfer_403";
		}
		return "goods/transfer_records";

	}
	
	@RequestMapping(value = "/confirm.html", method = RequestMethod.GET)
	public String confirm(
			@ModelAttribute("transfer") SearchParameter<Transfer> parameter,
			@RequestParam(required=false) Optional<String> applyAgent,
			HttpServletRequest request, Model model) {
		try {
			if(applyAgent.isPresent()){
				model.addAttribute("applyAgent", applyAgent.get());
			}
			User user = (User) WebUtil.getCurrentUser(request);
			parameter.getEntity().setStatus(GoodsTransferStatus.NEW);
			if(user.isAdmin()){
				MutedUser mutedUser=(MutedUser)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY);
				parameter.getEntity().setUserByFromAgent(mutedUser);
				List<Transfer> ts=transferDAO.searchEntityByPage(parameter, null, null,null, applyAgent);
				WebUtil.turnPage(parameter, request);
				model.addAttribute("trans", ts);
			}else{
				Agent agent=agentDAO.findById(user.getId());
				parameter.getEntity().setUserByFromAgent(agent);
				List<Transfer> ts=transferDAO.searchEntityByPage(parameter, null, null,null, applyAgent);
				WebUtil.turnPage(parameter, request);
				model.addAttribute("trans", ts);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			model.addAttribute("message", exp.getMessage());
			return "goods/transfer_403";
		}
		return "goods/transfer_confirm";

	}
	
	/*@RequestMapping(value = "/detail.html", method = RequestMethod.GET)
	public String detail(@ModelAttribute("transfer") SearchParameter<Transfer> parameter,HttpServletRequest request,Model model){
		try{
			//model.addAttribute("items",parameter.getEntity().getItems().values().stream().collect(Collectors.toList()));
			Transfer transfer=parameter.getEntity();
			List<TransferApplySuccessDTO> dtos=transfer.getItems().values().stream().map(item->{
				TransferApplySuccessDTO dto=new TransferApplySuccessDTO();
				dto.setGoodsName(item.getGoodsName());
				dto.setPrice(item.getPrice());
				dto.setAmount(item.getAmount());
				dto.setQuantity(item.getQuantity());
				dto.setLevelName(item.getPriceLevelName());
				return dto;
			}).collect(Collectors.toList());
			model.addAttribute("items",dtos);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return "goods/transfer_success";
	}*/
	
	@RequestMapping(value = {"/success.html","/detail.html"}, method = RequestMethod.GET)
	public String success(@ModelAttribute("transfer") SearchParameter<Transfer> parameter,HttpServletRequest request,Model model){
		try{
			Transfer transfer=parameter.getEntity();
			List<TransferApplySuccessDTO> dtos=transfer.getItems().values().stream().map(item->{
				TransferApplySuccessDTO dto=new TransferApplySuccessDTO();
				dto.setGoodsName(item.getGoodsName());
				dto.setPrice(item.getPrice());
				dto.setAmount(item.getAmount());
				dto.setQuantity(item.getQuantity());
				dto.setLevelName(item.getPriceLevelName());
				return dto;
			}).collect(Collectors.toList());
			model.addAttribute("items",dtos);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return "goods/transfer_success";
	}



	@RequestMapping(value = {"/calculate.html"}, method = RequestMethod.GET)
	public String caculate(HttpServletRequest request,Model model,String type){
		try{
			Transfer transfer=(Transfer)WebUtil.getSessionAttribute(request, "transfer_calculate");
			List<TransferApplySuccessDTO> dtos=transfer.getItems().values().stream().map(item->{
				TransferApplySuccessDTO dto=new TransferApplySuccessDTO();
				dto.setGoodsName(item.getGoodsName());
				dto.setPrice(item.getPrice());
				dto.setAmount(item.getAmount());
				dto.setQuantity(item.getQuantity());
				dto.setLevelName(item.getPriceLevelName());
				return dto;
			}).collect(Collectors.toList());
			model.addAttribute("items",dtos);
			model.addAttribute("transfer_calculate", transfer);
		}catch(Exception exp){
			exp.printStackTrace();
		}
		if(type!=null&&type.equals("mall")){
			return "goods/mall_transfer_calculate";
		}
		return "goods/transfer_calculate";
	}




	
	/*@ModelAttribute("parameter")
	public SearchParameter<Goods> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<Goods> parameter = new SearchParameter<Goods>();
		Goods goods = null;
		if (id.isPresent()) {
			goods = (Goods) goodsDAO.findById(id.get());
		} else {
			goods = new Goods();
		}
		parameter.setEntity(goods);
		return parameter;
	}*/
	
	@ModelAttribute("transfer")
	public SearchParameter<Transfer> getTransfer(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<Transfer> parameter = new SearchParameter<Transfer>();
		Transfer entity = null;
		if (id.isPresent()) {
			entity = (Transfer) transferDAO.findById(id.get());
		} else {
			entity = new Transfer();
		}
		parameter.setEntity(entity);
		return parameter;
	}

	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private TransferDAO transferDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private AgentLevelDAO agentLevelDAO;
	@Autowired
	private GoodsAccountDAO goodsAccountDAO;
	@Autowired
	private TransferHandler transferHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
