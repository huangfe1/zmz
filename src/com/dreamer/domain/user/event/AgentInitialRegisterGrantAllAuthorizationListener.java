package com.dreamer.domain.user.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentStatus;
import com.dreamer.repository.authorization.AuthorizationTypeDAO;
import com.dreamer.service.user.GiftPointHandler;

@Component
public class AgentInitialRegisterGrantAllAuthorizationListener implements
		AgentListener {

	@Override
	public void actionPerformed(AgentEvent event) {
		// TODO Auto-generated method stub
		Agent agent = event.getSource();
		grantMainGoodsAuthorizationTo(agent);
		LOG.info("{} 首次注册，赋予所有主打商品授权", agent.getAgentCode());
	}

	private void grantMainGoodsAuthorizationTo(Agent agent) {
		List<AuthorizationType> types = authorizationTypeDAO
				.findMainGoodsAuthorizationType();
		types.forEach((t) -> {
			Authorization auth = agent.buildAuthorization(t);
			auth.setStatus(AgentStatus.ACTIVE);
			agent.addAuthorization(auth);
		});
		agent.activeAll();
	}

	@Autowired
	private GiftPointHandler giftPointHandler;

	@Autowired
	private AuthorizationTypeDAO authorizationTypeDAO;

	private final Logger LOG = LoggerFactory.getLogger(getClass());

}
