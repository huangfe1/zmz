package com.dreamer.view.user;

import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.authorization.AuthorizationDAO;
import com.dreamer.repository.authorization.AuthorizationTypeDAO;
import com.dreamer.repository.user.AgentDAO;
import com.dreamer.repository.user.AgentLevelDAO;
import com.dreamer.repository.user.MutedUserDAO;
import com.dreamer.service.user.AgentHandler;
import com.dreamer.service.wxchat.NotcieHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ps.mx.otter.utils.Constant;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/agent")
public class AgentController {

	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(
			@ModelAttribute("parameter") Agent parameter,
			Integer[] ids,
			@RequestParam(value="parentAgentCode",required=false)String parentAgentCode,
			@RequestParam(value="teqparentAgentCode",required=false)String teqparentAgentCode,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			//User mutedUser=(User)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY);
			Agent parent=getParentFromCode(parentAgentCode,request);//获取上级
            Agent teqparent=getParentFromCode(teqparentAgentCode,request);//获取特权上级
            //判断是否存在
            if(Objects.isNull(parent)){
                return Message.createFailedMessage("上级代理编码["+parentAgentCode+"]对应的代理不存在");
            }
            if(Objects.isNull(teqparent)){
                return Message.createFailedMessage("上级代理编码["+teqparentAgentCode+"]对应的代理不存在");
            }
            if(user.isAdmin()){
				if(parameter.getId()==null){
					agentHandler.addAgent(user, parameter, ids, parent,teqparent);
				}else{
					agentHandler.updateAgent(user, parameter,ids, parent,teqparent);
				}
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("代理信息保存失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}

	@RequestMapping(value = "/sendMessage.json",method = RequestMethod.POST)
	private Message sendMessage(String title,String[] keywords, String template_id,String url,String remark ){
		int index=0;
        List<Agent> agents=agentDAO.findHasOpenId();//找到有openID的小伙伴
        for(Agent agent:agents){
			if(Objects.nonNull(agent.getWxOpenid())){
				if(	notcieHandler.sendMessage(agent.getWxOpenid(),url,template_id,title,keywords,remark)){
                    index++;
                }
			}
		}
        Message successMessage =Message.createSuccessMessage();
        successMessage.setData("本次成功发送"+index);
        return successMessage;

	}




    private Agent getParentFromCode(String code,HttpServletRequest request){
        Agent temAgent=null;
        User mutedUser=(User)WebUtil.getSessionAttribute(request, Constant.MUTED_USER_KEY);
        if(Objects.nonNull(code) && !code.isEmpty()){
            temAgent=agentDAO.findByAgentCode(code);
        }else{
            temAgent=mutedUserDAO.findById(mutedUser.getId());
        }
        return temAgent;
    }

	/**
	 * 用户审核
	 * @param parameter
	 * @param ids
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/audit.json", method = RequestMethod.POST)
	public Message audit(
			@ModelAttribute("parameter") Agent parameter,
			Integer[] ids,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
					agentHandler.auditAgent(user, parameter,ids);
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("代理信息通过审核失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}

	@RequestMapping(value = "/active.json", method = RequestMethod.POST)
	public Message active(
			@ModelAttribute("parameter") Agent parameter,
			Integer[] authedIds,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				agentHandler.activeAgent(user, parameter);
			}else if(user.isAgent()){
				return Message.createFailedMessage("无激活操作权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("代理激活失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/reorganize.json", method = RequestMethod.POST)
	public Message reorganize(
			@ModelAttribute("parameter") Agent parameter,HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				agentHandler.georganizeAgent(user, parameter);
			}else if(user.isAgent()){
				return Message.createFailedMessage("本操作仅限于管理员权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("代理信息保存失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(
			@ModelAttribute("parameter") Agent parameter,HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				agentHandler.removeAgent(user, parameter);
			}else if(user.isAgent()){
				return Message.createFailedMessage("本操作仅限于管理员权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("代理信息保存失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/transfer.json", method = RequestMethod.POST)
	public Message transfer(
			@ModelAttribute("parameter") Agent parameter,
			@RequestParam("toId") Integer toId,HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				Agent toAgent=agentDAO.findById(toId);
				agentHandler.transfer(parameter, toAgent, user);
			}else{
				return Message.createFailedMessage("本操作仅限于管理员权限");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("代理转让失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/auth/remove.json", method = RequestMethod.DELETE)
	public Message removeAuthorization(
			@ModelAttribute("parameter") Agent parameter,
			Integer authId,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				Authorization auth=authorizationDAO.findById(authId);
				agentHandler.removeAuthorization(user, parameter, auth);
			}else if(user.isAgent()){
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("取消代理授权失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}
	
	@RequestMapping(value = "/level/change.json", method = RequestMethod.POST)
	public Message changePriceLevel(
			@ModelAttribute("parameter") Agent parameter,
			Integer goodsId,
			Integer levelId,
			HttpServletRequest request,
			Model model) {
		try{
			User user=(User)WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				agentHandler.changePriceLevel(parameter, goodsId, levelId);
			}else{
				return Message.createFailedMessage("非管理员角色操作");
			}
			return Message.createSuccessMessage();
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("取消代理授权失败",exp);
			return Message.createFailedMessage(exp.getMessage());
		}		
	}

	@ModelAttribute("parameter")
	public Agent preprocess(
			@RequestParam("id") Optional<Integer> id) {
		Agent agent = null;
		if (id.isPresent()) {
			agent = (Agent)agentDAO.findById(id.get());
		} else {
			agent = Agent.build();
		}
		return agent;
	}

	@Autowired
	private AgentLevelDAO agentLevelDAO;
	@Autowired
	private AgentDAO agentDAO;
	@Autowired
	private AuthorizationTypeDAO authorizationTypeDAO;
	@Autowired
	private AuthorizationDAO authorizationDAO;
	@Autowired
	private AgentHandler agentHandler;
	@Autowired
	private MutedUserDAO mutedUserDAO;
	@Autowired
	private NotcieHandler notcieHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
