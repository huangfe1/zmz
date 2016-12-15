package com.dreamer.domain.user.event;

import java.util.EventObject;

import com.dreamer.domain.user.Agent;

public class AgentEvent extends EventObject {

	private static final long serialVersionUID = -4977061077763368228L;

	private Agent agent;
	
	public AgentEvent(Agent source) {
		super(source);
		agent=source;
	}

	@Override
	public Agent getSource() {
		// TODO Auto-generated method stub
		return agent;
	}
	
}
