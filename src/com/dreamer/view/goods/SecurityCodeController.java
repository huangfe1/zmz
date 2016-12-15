package com.dreamer.view.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.NotAuthorizationException;
import ps.mx.otter.utils.WebUtil;
import ps.mx.otter.utils.message.Message;

import com.dreamer.domain.goods.SecurityCode;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.SecurityCodeDAO;
import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.service.goods.SecurityCodeHandler;
import ps.mx.otter.utils.storage.LifetimeData;

@RestController
@RequestMapping("/securityCode")
public class SecurityCodeController {


	@RequestMapping(value = "/edit.json", method = RequestMethod.POST)
	public Message edit(
			@ModelAttribute("parameter") SecurityCode parameter,
			@RequestParam("codeSegment") String codeSegment,
			HttpServletRequest request, Model model) {
		try{
			User user = (User) WebUtil.getCurrentUser(request);
			if(parameter.getVersion()==null){
				securityCodeHandler.addCodeSegment(user,parameter,codeSegment);
			}else{
				securityCodeHandler.updateSegment(user,parameter,codeSegment);
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("防伪码保存失败,",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}


	/**
	 * 录入防伪码  扫描传过来的
	 * @param parameter
	 * @param codeSegment
	 * @param request
	 * @param model
     * @return
     */
	@RequestMapping(value = "/scanEdit.json", method = RequestMethod.POST)
	public Message scanEdit(@ModelAttribute("parameter") SecurityCode parameter,String[] codes,
			HttpServletRequest request, Model model) {
		try{
            System.out.println(parameter.getGoodsName());
            System.out.println(parameter.getOwner());
            System.out.println(parameter.getAgentCode());
            if(Objects.isNull(codes)){
                throw new ApplicationException();
            }
			User user = (User) WebUtil.getCurrentUser(request);
            List<String> list=new ArrayList<>();
            for(String str:codes){
                list.add(str);
            }
            securityCodeHandler.addCodeSegmentWithNum(user,parameter,list);

		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("防伪码录入失败,",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}
	
	@RequestMapping(value = "/editWithNum.json", method = RequestMethod.POST)
	public Message editWithNum(
			@ModelAttribute("parameter") SecurityCode parameter,
			@RequestParam("codeSegment") String codeSegment,
			@RequestParam("quantity") Integer quantity,
			HttpServletRequest request, Model model) {
		try{
			if(Objects.isNull(quantity) || quantity<1){
				throw new ApplicationException("数量输入错误");
			}
			User user = (User) WebUtil.getCurrentUser(request);
			if(parameter.getVersion()==null){
				securityCodeHandler.addCodeSegmentWithNum(user,parameter,codeSegment,quantity);
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("防伪码保存失败,",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}
	
	@RequestMapping(value = "/remove.json", method = RequestMethod.DELETE)
	public Message remove(
			@ModelAttribute("parameter") SecurityCode parameter,
			HttpServletRequest request, Model model) {
		try{
			User user = (User) WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				securityCodeHandler.remove(parameter);
			}else{
				throw new NotAuthorizationException("无删除防伪码权限");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("防伪码删除失败,",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}
	
	@RequestMapping(value = "/rang-rm.json", method = RequestMethod.POST)
	public Message removeRange(
			@ModelAttribute("parameter") SecurityCode parameter,
			@RequestParam(value="agent") String agentCode,
			@RequestParam(value="code") String code,
			HttpServletRequest request, Model model) {
		try{
			User user = (User) WebUtil.getCurrentUser(request);
			if(user.isAdmin()){
				
				parameter.setAgentCode(agentCode);
				securityCodeHandler.removeRange(parameter,code);
			}else{
				throw new NotAuthorizationException("无删除防伪码权限");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("防伪码删除失败,",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}
	
	@RequestMapping(value = "/rang-rm-num.json", method = RequestMethod.POST)
	public Message removeRangeWithNum(
			@ModelAttribute("parameter") SecurityCode parameter,
			@RequestParam(value="agent") String agentCode,
			@RequestParam(value="code") String code,
			@RequestParam("quantity") Integer quantity,
			HttpServletRequest request, Model model) {
		try{
			if(Objects.isNull(quantity) || quantity<1){
				throw new ApplicationException("数量输入错误");
			}
			User user = (User) WebUtil.getCurrentUser(request);
			if(user.isAdmin()){				
				parameter.setAgentCode(agentCode);
				securityCodeHandler.removeRangeWithNum(parameter,code,quantity);
			}else{
				throw new NotAuthorizationException("无删除防伪码权限");
			}
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("防伪码删除失败,",exp);
			return Message.createFailedMessage(exp.getMessage());
		}
		return Message.createSuccessMessage();
	}
	


	

	@ModelAttribute("parameter")
	public SecurityCode preprocess(
			@RequestParam("id") Optional<Integer> id) {
		SecurityCode code = null;
		if (id.isPresent()) {
			code = (SecurityCode) securityCodeDAO.findById(id.get());
		} else {
			code = new SecurityCode();
		}
		return code;
	}

	@Autowired
	private SecurityCodeDAO securityCodeDAO;
	
	@Autowired
	private SecurityCodeHandler securityCodeHandler;
	
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
