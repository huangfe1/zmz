package com.dreamer.view.user;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.PointsTransfer;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.repository.user.PointsTransferDAO;
import com.dreamer.repository.user.UserDAO;

@Controller
@RequestMapping("/points")
public class PointsTransferQueryController {

	@RequestMapping(value = "/my.html", method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<PointsTransfer> parameter,
			Model model, HttpServletRequest request) {
		try {
			List<PointsTransfer> pts = new ArrayList<PointsTransfer>();
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAdmin()) {
				pts = pointsTransferDAO.searchEntityByPage(parameter, null,
						null, null);
			} else {
				pts = pointsTransferDAO.searchEntityByPage(parameter, null,
						null, user.getId());
			}

			WebUtil.turnPage(parameter, request);
			model.addAttribute("pts", pts);
			model.addAttribute("from", user.getId());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return "user/points_my";
	}

	@RequestMapping(value = "/transfer.html", method = RequestMethod.GET)
	public String transfer(
			@ModelAttribute("parameter") SearchParameter<PointsTransfer> parameter,
			Model model, HttpServletRequest request) {
		String url="user/points_transfer";
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			Agent agent=null;
			if (user.isAgent()) {
				agent = agentDAO.findById(user.getId());
			}else if(user.isAdmin()){
				agent=mutedUserDAO.loadFirstOne();
				url="user/points_admin_transfer";
			}
			parameter.getEntity().setUserByFromAgent(agent);
			model.addAttribute("fromAgent", agent);
			model.addAttribute("pointsBalance", agent.getPointsBalance());
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return url;
	}

	@RequestMapping(value = "/query.json", method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<PointsTransfer> queryAsJson(
			@ModelAttribute("parameter") SearchParameter<PointsTransfer> parameter,
			Model model, HttpServletRequest request) {
		DatatableDTO<PointsTransfer> dts = new DatatableDTO<PointsTransfer>();
		try {
			List<PointsTransfer> pts = new ArrayList<PointsTransfer>();
			User user = (User) WebUtil.getCurrentUser(request);
			if (!user.isAdmin()) {
				pts = pointsTransferDAO.searchEntityByPage(parameter, null,
						null, null);
			} else {
				pts = pointsTransferDAO.searchEntityByPage(parameter, null,
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
	public SearchParameter<PointsTransfer> preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SearchParameter<PointsTransfer> parameter = new SearchParameter<PointsTransfer>();
		if (id.isPresent()) {
			parameter.setEntity(pointsTransferDAO.findById(id.get()));
		} else {
			parameter.setEntity(new PointsTransfer());
		}
		return parameter;
	}

	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PointsTransferDAO pointsTransferDAO;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
