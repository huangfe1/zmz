package com.dreamer.view.user;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.user.AgentLevel;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.service.user.AgentLevelHandler;

@RestController
@RequestMapping("/agentLevel")
public class AgentLevelController {

	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(@ModelAttribute("parameter") AgentLevel parameter,
			Model model) {
		try {
			if (parameter.getId() == null) {
				agentLevelHandler.addAgentLevel(parameter);
			} else {
				agentLevelHandler.updateAgentLevel(parameter);
			}
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			LOG.error("代理等级维护失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@RequestMapping(value = "/remove.json",method=RequestMethod.DELETE)
	public Message remove(@ModelAttribute("parameter") AgentLevel parameter,
			Model model) {
		try {
			agentLevelHandler.removeAgentLevel(parameter);
			return Message.createSuccessMessage();
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("删除代理等级失败");
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public AgentLevel preprocess(@RequestParam("id") Optional<Integer> id) {
		AgentLevel level = null;
		if (id.isPresent()) {
			level = agentLevelDAO.findById(id.get());
		} else {
			level = new AgentLevel();
		}
		return level;
	}

	@Autowired
	private AgentLevelDAO agentLevelDAO;

	@Autowired
	private AgentLevelHandler agentLevelHandler;
	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
