package com.dreamer.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.system.SystemParameter;
import com.dreamer.repository.system.SystemParameterDAOInf;

@Service("systemParameterHandler")
public class SystemParameterHandler {

	@Transactional
	public void addParameter(SystemParameter param){
			systemParameterDAO.save(param);
	}

	@Transactional
	public void updateParameter(SystemParameter param){
		systemParameterDAO.merge(param);
	}
	
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
}
