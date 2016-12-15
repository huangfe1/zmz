package com.dreamer.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamer.domain.user.AgentLevel;
import com.dreamer.repository.user.AgentLevelDAO;

@Service
public class AgentLevelHandler {

	@Transactional
	public void addAgentLevel(AgentLevel level){
		if(level.getParent()!=null && level.getParent().getId()!=null){
			AgentLevel parent=agentLevelDAO.findById(level.getParent().getId());
			level.setParent(parent);
			level.setLevel(parent.getLevel()+1);
		}else{
			level.setLevel(1);
			level.setParent(null);
		}
		agentLevelDAO.merge(level);
	}
	
	@Transactional
	public void removeAgentLevel(AgentLevel level){
		agentLevelDAO.delete(level);
	}
	
	@Transactional
	public void updateAgentLevel(AgentLevel level){
		AgentLevel parent=level.getParent();
		if(parent==null || parent.getId()==null){
			level.setParent(null);
		}
		agentLevelDAO.merge(level);
	}
	
	@Autowired
	private AgentLevelDAO agentLevelDAO;
}
