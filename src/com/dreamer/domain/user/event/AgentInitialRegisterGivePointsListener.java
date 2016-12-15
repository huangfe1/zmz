package com.dreamer.domain.user.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dreamer.domain.user.Agent;
import com.dreamer.service.user.GiftPointHandler;

@Component
public class AgentInitialRegisterGivePointsListener implements AgentListener {

	@Override
	public void actionPerformed(AgentEvent event) {
		// TODO Auto-generated method stub
		Agent agent=event.getSource();
		int point=giftPointHandler.giftPointToAgent(agent);
		Agent parent=agent.getParent();
		if(!parent.isMutedUser()){
			giftPointHandler.giftPointToAgent(parent);
		}
		LOG.info("{} 首次注册，赠送积分 {}",agent.getAgentCode(),point);
	}
	
	@Autowired
	private GiftPointHandler giftPointHandler;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());

}
