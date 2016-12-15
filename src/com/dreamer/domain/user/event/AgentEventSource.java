package com.dreamer.domain.user.event;

public interface AgentEventSource {

	public void addAgentListener(AgentListener listener);
	public void fireEvent(AgentEvent agentEvent);
}
