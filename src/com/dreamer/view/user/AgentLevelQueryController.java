package com.dreamer.view.user;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.goods.GoodsType;
import com.dreamer.domain.user.AgentLevel;
import com.dreamer.repository.user.AgentLevelDAO;

@Controller
@RequestMapping("/agentLevel")
public class AgentLevelQueryController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SearchParameter<AgentLevel> parameter,
			Model model, HttpServletRequest request) {
		try {
			List<AgentLevel> levels = agentLevelDAO.searchEntityByPage(
					parameter, null, (t) -> {
						return true;
					});
			WebUtil.turnPage(parameter, request);
			
			model.addAttribute("levels", levels);
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("代理等级查询失败",exp);
		}
		return "/user/agentLevel_index";
	}

	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("parameter") SearchParameter<AgentLevel> parameter,
			Model model) {
		model.addAttribute("types", GoodsType.values());//等级类型
		return "/user/agentLevel_edit";
	}
	
	@RequestMapping(value = "/goods.html", method = RequestMethod.GET)
	public String goodsList(
			@ModelAttribute("parameter") SearchParameter<AgentLevel> parameter,
			Model model) {
		return "/user/agentLevel_goods";
	}

	@ModelAttribute("parameter")
	public SearchParameter<AgentLevel> preprocess(
			@RequestParam("id") Optional<Integer> id,
			@RequestParam("parent") Optional<Integer> parentId) {
		AgentLevel level = null;
		if (id.isPresent()) {
			level = agentLevelDAO.findById(id.get());
		} else {
			level = new AgentLevel();
		}
		if (parentId.isPresent()) {
			AgentLevel parent = agentLevelDAO.findById(parentId.get());
			level.setParent(parent);
		}
		return new SearchParameter<AgentLevel>(level);
	}

	@Autowired
	private AgentLevelDAO agentLevelDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
