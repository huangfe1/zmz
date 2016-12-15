package com.dreamer.view.system;

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

import com.dreamer.domain.system.SystemParameter;
import com.dreamer.repository.system.SystemParameterDAOInf;

@Controller
@RequestMapping("/system/param")
public class SystemParameterQueryController {

	@RequestMapping(value="/index.html",method=RequestMethod.GET)
	public String index(@ModelAttribute("parameter")SearchParameter<SystemParameter> param,Model model,HttpServletRequest request){
		try{
			List<SystemParameter> sysParams= systemParameterDAO.searchEntityByPage(param, null, (t)->true);
			WebUtil.turnPage(param, request);
			model.addAttribute("sysParams", sysParams);
		}catch(Exception exp){
			LOG.error("系统参数查询失败",exp);
			exp.printStackTrace();
		}
		return "/system/param_index";
	}
	
	@RequestMapping(value="/edit.html",method=RequestMethod.GET)
	public String edit_enter(@ModelAttribute("parameter")SearchParameter<SystemParameter> param,Model model,HttpServletRequest request){
		return "/system/param_edit";
	}
	
	@ModelAttribute("parameter")
	public SearchParameter<SystemParameter> preprocess(@RequestParam("id") Optional<String> id){
		SystemParameter param=null;
		if(id.isPresent()){
			param=systemParameterDAO.findById(id.get());
		}else{
			param=new SystemParameter();
		}
		SearchParameter<SystemParameter> sp=new SearchParameter<SystemParameter>(param);
		return sp;
	}
	
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
