package com.dreamer.service.user.agentCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.repository.system.SystemParameterDAOInf;
@Component("defaultAgentCodeGenerator")
public class DefaultAgentCodeGenerator extends AgentCodeGeneratorTemplate {

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
	@Transactional
	public String buildBody() {
		// TODO Auto-generated method stub
		String initVal= systemParameterDAO.getSeedOfAgent();
		LOG.debug("获取代理编码主体种子值：{}",initVal);
		int bodyLen=systemParameterDAO.getAgentCodeBodyLen();
		LOG.debug("获取代理编码主体长度：{}",bodyLen);
		String padding=systemParameterDAO.getAgentCodePadding();
		LOG.debug("获取代理编码主体占位符：{}",padding);
		initVal=initVal.replace(AVOID_CODE, String.valueOf((Integer.parseInt(AVOID_CODE)+1)));
		LOG.debug("替换代理编码主体中的回避字符：{} 为：{}",AVOID_CODE,initVal);
		systemParameterDAO.updateSeedOfAgent(String.valueOf(Integer.parseInt(initVal)+1));
		StringBuilder stb=new StringBuilder(bodyLen);
		if(initVal.length()<bodyLen){
			for(int index=bodyLen-initVal.length();index>0;index--){
				stb.append(padding);
			}
		}
		stb.append(initVal);
		LOG.debug("生成代理编码主体：{}",stb.toString());
		return stb.toString();
	}
	
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
	
}
