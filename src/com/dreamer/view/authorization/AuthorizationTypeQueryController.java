package com.dreamer.view.authorization;

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
import org.springframework.web.bind.annotation.ResponseBody;

import ps.mx.otter.utils.DatatableDTO;
import ps.mx.otter.utils.SearchParameter;
import ps.mx.otter.utils.WebUtil;

import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.domain.user.User;
import com.dreamer.repository.authorization.AuthorizationTypeDAO;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.user.AgentLevelDAO;

@Controller
@RequestMapping("/authType")
public class AuthorizationTypeQueryController {
	
	@RequestMapping(value = { "/index.html", "/search.html" }, method = RequestMethod.GET)
	public String index(@ModelAttribute("parameter")SearchParameter<AuthorizationType> parameter,HttpServletRequest request,Model model){
		try{
			List<AuthorizationType> authType=authorizationTypeDAO.searchEntityByPage(parameter, null, (t)->true);
			WebUtil.turnPage(parameter, request);
			model.addAttribute("authTypes", authType);
		}catch(Exception exp){
			exp.printStackTrace();
			LOG.error("授权类型查询失败",exp);
		}
		return "/authorization/authType_index";
	}
	
	@RequestMapping(value = { "/query.json"}, method = RequestMethod.GET)
	@ResponseBody
	public DatatableDTO<AuthorizationType> queryAuthsAsJson(
			@ModelAttribute("parameter") SearchParameter<AuthorizationType> parameter,
			Model model, HttpServletRequest request) {
		DatatableDTO<AuthorizationType> dts=new DatatableDTO<AuthorizationType>();
		try {
			List<AuthorizationType> auths=authorizationTypeDAO.searchEntityByPage(parameter, null, (t)->true);
			WebUtil.turnPage(parameter, request);
			dts.setData(auths);
			dts.setRecordsTotal(parameter.getTotalRows());
			dts.setRecordsFiltered(parameter.getTotalRows());
		} catch (Exception exp) {
			exp.printStackTrace();
			LOG.error("授权类型查询失败",exp);
		}
		return dts;
	}
	
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public String edit_enter(
			@ModelAttribute("parameter") SearchParameter<AuthorizationType> parameter,HttpServletRequest request,
			Model model) {
		User user=(User)WebUtil.getCurrentUser(request);
		if(user.isAdmin()){
			return "/authorization/authType_edit";
		}else{
			LOG.error("非管理员身份,无授权类型编辑权限");
			return "/common/403";
		}
		
	}
	
	
	@ModelAttribute("parameter")
	public SearchParameter<AuthorizationType> preprocess(@RequestParam("id")Optional<Integer> id){
		SearchParameter<AuthorizationType> parameter=new SearchParameter<AuthorizationType>();
		AuthorizationType authType=null;
		if(id.isPresent()){
			authType=(AuthorizationType)authorizationTypeDAO.findById(id.get());
		}else{
			authType=new AuthorizationType();
		}
		parameter.setEntity(authType);
		return parameter;
	}
	
	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentLevelDAO agentLevelDAO;
	@Autowired
	private AuthorizationTypeDAO authorizationTypeDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
