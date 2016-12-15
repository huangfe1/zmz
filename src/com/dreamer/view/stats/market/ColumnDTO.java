package com.dreamer.view.stats.market;

import java.util.ArrayList;
import java.util.List;

public class ColumnDTO {

	private String name;
	private Integer id;
	private List<AgentDTO> agents=new ArrayList<AgentDTO>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public List<AgentDTO> getAgents() {
		return agents;
	}
	public void setAgents(List<AgentDTO> agents) {
		this.agents = agents;
	}
	
	
}
