package com.dreamer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dreamer.domain.user.Agent;
import com.dreamer.repository.system.SystemParameterDAOInf;

@Component
public class GiftPointHandler {

	public Integer giftPointToAgent(Agent agent){
		int points=systemParameterDAO.getGiftPoints();
		agent.giftPoint(points);
		return points;
	}
	
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
}
