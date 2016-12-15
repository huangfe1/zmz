package com.dreamer.service.user;

import org.springframework.stereotype.Component;

import ps.mx.otter.utils.digest.DigestToolHMAC;

import com.dreamer.domain.user.Agent;

@Component
public class AgentDomainGenerator implements AgentDomainGenerateStrategy {

	private static final DigestToolHMAC HMAC=new DigestToolHMAC();
	@Override
	public String gererateSubDomain(Agent agent) {
		// TODO Auto-generated method stub
		StringBuilder sbd=new StringBuilder();
		sbd.append("/dmz/vmall/").append(agent.getAgentCode()).append("/index.html");
		String token=HMAC.generateDigest(sbd.toString(), agent.getAgentCode());
		return sbd.append("?token=").append(token).toString();
	}

}
