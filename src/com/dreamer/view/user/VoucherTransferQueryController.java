package com.dreamer.view.user;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.domain.user.VoucherTransfer;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.repository.user.UserDAO;
import com.dreamer.repository.user.VoucherTransferDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/voucher")
public class VoucherTransferQueryController {

	@RequestMapping(value = "/my.html", method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<VoucherTransfer> parameter,
			Model model, HttpServletRequest request,String toAgent) {
		try {
            Agent atmpt=agentDAO.findByAgentCode(toAgent);
            parameter.getEntity().setUserByToAgent(atmpt);//设置转出人
			List<VoucherTransfer> pts;
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAdmin()) {
				pts = voucherTransferDAO.searchEntityByPage(parameter, null,
						null, null);
			} else {
				pts = voucherTransferDAO.searchEntityByPage(parameter, null,
						null, user.getId());
			}
			WebUtil.turnPage(parameter, request);
			model.addAttribute("pts", pts);
			model.addAttribute("from", user.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "user/voucher_my";
	}
	
	

	@RequestMapping(value = "/transfer.html", method = RequestMethod.GET)
	public String transfer(
			@ModelAttribute("parameter") SearchParameter<VoucherTransfer> parameter,
			Model model, HttpServletRequest request) {
		String url="user/voucher_transfer";
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent=null;
			if (user.isAgent()) {
				//return "common/403";
				agent = agentDAO.findById(user.getId());
			}else if(user.isAdmin()){
				agent=mutedUserDAO.loadFirstOne();
				url="user/voucher_admin_transfer";
			}
			parameter.getEntity().setUserByFromAgent(agent);
			model.addAttribute("fromAgent", agent);
			model.addAttribute("voucherBalance", agent.getAccounts().getVoucherBalance());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return url;
	}
	
	

	@RequestMapping(value = "/query.json", method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<VoucherTransfer> queryAsJson(
			@ModelAttribute("parameter") SearchParameter<VoucherTransfer> parameter,
			Model model, HttpServletRequest request) {
		DatatableDTO<VoucherTransfer> dts = new DatatableDTO<VoucherTransfer>();
		try {
			List<VoucherTransfer> pts = new ArrayList<VoucherTransfer>();
			User user = (User) WebUtil.getCurrentUser(request);
			if (!user.isAdmin()) {
				pts = voucherTransferDAO.searchEntityByPage(parameter, null,
						null, null);
			} else {
				pts = voucherTransferDAO.searchEntityByPage(parameter, null,
						null, user.getId());
			}
			WebUtil.turnPage(parameter, request);
			dts.setData(pts);
			dts.setRecordsFiltered(pts.size());
			dts.setRecordsTotal(pts.size());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return dts;
	}

	@ModelAttribute("parameter")
	public SearchParameter<VoucherTransfer> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<VoucherTransfer> parameter = new SearchParameter<VoucherTransfer>();
		if (id.isPresent()) {
			parameter.setEntity(voucherTransferDAO.findById(id.get()));
		} else {
			parameter.setEntity(new VoucherTransfer());
		}
		return parameter;
	}

	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private VoucherTransferDAO voucherTransferDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
