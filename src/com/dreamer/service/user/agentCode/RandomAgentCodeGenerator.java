package com.dreamer.service.user.agentCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.service.pay.RandomStringGenerator;

@Component("randomAgentCodeGenerator")
public class RandomAgentCodeGenerator extends AgentCodeGeneratorTemplate {

	private static final String AVOID_CODE="4";
	
	@Override
	public String buildPrefix() {
		// TODO Auto-generated method stub
		return systemParameterDAO.getPrefixOfAgent();
	}

	@Override
	public String buildSuffix() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String buildBody() {
		// TODO Auto-generated method stub
		String random=RandomStringGenerator.getRandomNumberStringByLength(6);
		LOG.debug("随机代码编码后缀:{}",random);
		return random.replace(AVOID_CODE, String.valueOf((Integer.parseInt(AVOID_CODE)+1)));
	}
	
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
