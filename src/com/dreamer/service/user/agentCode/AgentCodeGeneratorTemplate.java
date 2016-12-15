package com.dreamer.service.user.agentCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public abstract class AgentCodeGeneratorTemplate implements AgentCodeGenerator {

	@Override
	@Transactional
	public String generateAgentCode() {
		// TODO Auto-generated method stub
		StringBuilder stb=new StringBuilder();
		stb.append(buildPrefix()).append(buildBody()).append(buildSuffix());
		LOG.debug("生成代理编码：{}",stb.toString());
		return stb.toString();
	}
	
	public abstract String buildPrefix();
	
	public abstract String buildSuffix();
	
	public abstract String buildBody();
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
