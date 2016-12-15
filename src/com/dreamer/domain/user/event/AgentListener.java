package com.dreamer.domain.user.event;

import java.util.EventListener;

public interface AgentListener extends EventListener {

	public void actionPerformed(AgentEvent event);
	
}
