package com.dreamer.view.mall;

import com.dreamer.domain.user.Agent;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.service.user.AgentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.date.DateUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/vmall/uc")
public class UserCenterController {

	@RequestMapping(value = "/dmz/{agentCode}/register.json", method = RequestMethod.POST)
	public Message register(@PathVariable("agentCode") String agentCode,
			@ModelAttribute("parameter")Agent agent,
			Model model, HttpServletRequest request,HttpServletResponse response) {
		try{
			Agent newAgent=agentHandler.selfRegister(agent, agentCode);
			String purl=(String)request.getSession().getAttribute("purl");
			if(Objects.isNull(purl)){
				UriComponents ucb = ServletUriComponentsBuilder
					.fromContextPath(request).path("/vmall/uc/index.html").queryParam("new", "1").build();
				purl=ucb.toUriString();
			}

			response.setHeader("Location",purl );
			request.getSession().removeAttribute("purl");
			LOG.debug("New Agent id:{}",newAgent.getId());
			WebUtil.addCurrentUser(request, newAgent);
			WebUtil.addLastVisitIp(request, request.getRemoteAddr());
			WebUtil.addLastVisitTime(request, DateUtil.date2string(new Date(), DateUtil.DATE_TIME_FORMAT));
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("用户自助注册失败",exp.getMessage());
			return Message.createFailedMessage(exp.getMessage());
		}
	}

	@ModelAttribute("parameter")
	public Agent preprocess(){
		return new Agent();
	}
	
	@Autowired
	private AgentHandler agentHandler;
	@Autowired
	private AgentDAO agentDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
