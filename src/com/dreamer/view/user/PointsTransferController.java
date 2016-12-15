package com.dreamer.view.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.PointsTransfer;
import com.dreamer.domain.user.User;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.repository.user.PointsTransferDAO;
import com.dreamer.repository.user.UserDAO;
import com.dreamer.service.user.AgentHandler;

@RestController
@RequestMapping("/points")
public class PointsTransferController {

	@RequestMapping(value = "/transfer.json", method = RequestMethod.POST)
	public Message transfer(
			@ModelAttribute("parameter") PointsTransfer transfer,
			@RequestParam("realName") String realName,
			@RequestParam("agentCode") String agentCode, Model model,
			HttpServletRequest request) {
		try {
			User user = (User) WebUtil.getCurrentUser(request);
			if (user.isAgent()) {
				Agent agent = agentDAO.findById(user.getId());
				transfer.setUserByFromAgent(agent);
				agentHandler.transferPoints(user, transfer, agentCode,
						realName, transfer.getPoint());
			}
			if (user.isAdmin()) {
				Agent agent = mutedUserDAO.loadFirstOne();
				transfer.setUserByFromAgent(agent);
				agentHandler.transferPoints(user, transfer, agentCode,
						realName, transfer.getPoint());
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public PointsTransfer preprocess(@RequestParam("id") Optional<Integer> id) {
		PointsTransfer parameter = new PointsTransfer();
		if (id.isPresent()) {
			parameter = pointsTransferDAO.findById(id.get());
		} else {
			parameter = new PointsTransfer();
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
	private AgentHandler agentHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;
}
