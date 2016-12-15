package com.dreamer.view.goods;

import com.dreamer.domain.goods.SecurityCode;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentStatus;
import com.dreamer.repository.goods.SecurityCodeDAO;
import com.dreamer.repository.user.AgentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/dmz/securityCode")
public class SecurityCodeDMZController {

	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(
			@ModelAttribute("parameter") SecurityCode parameter,@RequestParam(value = "f", required = false) String f,
			HttpServletRequest request, Model model) {
		try {
			String queryCode=parameter.getCode();
			if(Objects.nonNull(queryCode)){
				parameter.setCode(queryCode.replaceFirst("^0*", ""));//去掉前缀0
			}
			List<SecurityCode> codes = securityCodeDAO.findByCode(parameter.getCode());
			List<SecurityCode> temps=new ArrayList<>();
			for(SecurityCode code:codes){
				Agent agent_temp=agentDAO.findByAgentCode(code.getAgentCode());
				if(agent_temp.getAgentStatus()!=AgentStatus.ACTIVE) codes.remove(code);//只添加代理没有整顿的代理编号
			}
			model.addAttribute("codes", codes);//返回筛选后的
			model.addAttribute("f", f);
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("防伪码查询失败", exp);
		}
		return "goods/dmz/securityCode_search";
	}


	@ModelAttribute("parameter")
	public SecurityCode preprocess() {
		SecurityCode parameter = new SecurityCode();
		return parameter;
	}

	@Autowired
	private AgentDAO agentDAO;

	@Autowired
	private SecurityCodeDAO securityCodeDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
