package com.dreamer.service.goods;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.SecurityCode;
import com.dreamer.domain.goods.SecurityCodeSegment;
import com.dreamer.domain.goods.SecurityCodeSegmentWithNum;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.User;
import com.dreamer.repository.goods.GoodsDAO;
import com.dreamer.repository.goods.SecurityCodeDAO;
import com.dreamer.repository.system.SystemParameterDAOInf;
import com.dreamer.repository.user.AgentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ps.mx.otter.exception.ApplicationException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SecurityCodeHandler {
	
	@Transactional
	public void addCodeSegment(User user,SecurityCode securityCode,String codeSegment){
		Agent agent=validateAgent(securityCode);
		List<String> segmentCodes=generateSecurityCodes(codeSegment);
		if(user.isAgent()){
			validateCode(user, segmentCodes);		
			if(!agent.isMyParent(user)){
				throw new ApplicationException("代理只能给自己的下级录入防伪码");
			}
		}
		List<SecurityCode> codes=new ArrayList<SecurityCode>(segmentCodes.size());
		segmentCodes.forEach(s->{
			SecurityCode code=new SecurityCode();
			code.setCode(s);
			code.setGoodsName(securityCode.getGoodsName());
			code.setRecorder(user.getRealName());
			code.setOwner(agent.getRealName());
			code.setAgentCode(agent.getAgentCode());
			codes.add(code);
		});
		if(user.isAgent()){
			securityCodeDAO.batchRemoveAndSave(codes, ((Agent)user).getAgentCode());
		}else{
			securityCodeDAO.batchSave(codes);
		}
		
	}
	
	@Transactional
	public void addCodeSegmentWithNum(User user,SecurityCode securityCode,String codeSegment,Integer quantity){
		Agent agent=validateAgent(securityCode);
		List<String> segmentCodes=generateSecurityCodesWithNum(codeSegment,quantity);
		if(user.isAgent()){
			validateCode(user, segmentCodes);		
			if(!agent.isMyParent(user)){
				throw new ApplicationException("代理只能给自己的下级录入防伪码");
			}
		}
		List<SecurityCode> codes=new ArrayList<SecurityCode>(segmentCodes.size());
		segmentCodes.forEach(s->{
			SecurityCode code=new SecurityCode();
			code.setCode(s);
			code.setGoodsName(securityCode.getGoodsName());
			code.setRecorder(user.getRealName());
			code.setOwner(agent.getRealName());
			code.setAgentCode(agent.getAgentCode());
			codes.add(code);
		});
		if(user.isAgent()){
			securityCodeDAO.batchRemoveAndSave(codes, ((Agent)user).getAgentCode());
		}else{
			securityCodeDAO.batchSave(codes);
		}
		
	}



	@Transactional
	public void addCodeSegmentWithNum(User user,SecurityCode securityCode,List<String> codes){
		Agent agent=validateAgent(securityCode);
		if(user.isAgent()){
			validateCode(user, codes);
			if(!agent.isMyParent(user)){
				throw new ApplicationException("代理只能给自己的下级录入防伪码");
			}
		}
		List<SecurityCode> codesTemp=new ArrayList<>(codes.size());
		for (String s:codes){
			SecurityCode code=new SecurityCode();
			code.setCode(s);
			code.setGoodsName(securityCode.getGoodsName());
			code.setRecorder(user.getRealName());
			code.setOwner(agent.getRealName());
			code.setAgentCode(agent.getAgentCode());
			codesTemp.add(code);
		}

		if(user.isAgent()){
			securityCodeDAO.batchRemoveAndSave(codesTemp, ((Agent)user).getAgentCode());
		}else{
			securityCodeDAO.batchSave(codesTemp);
		}

	}
	
	
	
	@Transactional
	public void updateSegment(User user,SecurityCode securityCode,String codeSegment){
		if(!goodsIsExists(securityCode.getGoodsName())){
			throw new ApplicationException("所录入的商品名称不存在");
		}
		Agent agent=agentDAO.findByAgentCode(securityCode.getAgentCode());
		if(Objects.isNull(agent)){
			throw new ApplicationException("代理编码"+securityCode.getAgentCode()+"对应的代理信息不存在");
		}
		if(!Objects.equals(agent.getRealName(),securityCode.getOwner())){
			throw new ApplicationException("代理编码和姓名不匹配");
		}
		List<String> segmentCodes=generateSecurityCodes(codeSegment);
		if(user.isAgent()){
			validateCode(user, segmentCodes);	
			if(!agent.isMyParent(user)){
				throw new ApplicationException("代理只能给自己的下级录入防伪码");
			}
		}
		List<SecurityCode> codes=new ArrayList<SecurityCode>(segmentCodes.size());
		segmentCodes.forEach(s->{
			SecurityCode code=new SecurityCode();
			code.setCode(s);
			code.setGoodsName(securityCode.getGoodsName());
			code.setRecorder(user.getRealName());
			code.setOwner(agent.getRealName());
			code.setAgentCode(agent.getAgentCode());
			codes.add(code);
		});
		remove(securityCode);
		securityCodeDAO.batchSave(codes);
	}

	public List<String> generateSecurityCodes(String codeSegment){
		List<String> securityCodes=new ArrayList<String>();
		String[] codeGroup=codeSegment.split(systemParameterDAO.getSecurityCodeDelimiter());
		for(String group:codeGroup){
			SecurityCodeSegment segment=new SecurityCodeSegment(systemParameterDAO.getSecurityCodeLength(),systemParameterDAO.getPrefixOfSecurityCode(),group);
			securityCodes.addAll(segment.generateCodes());
		}
		return securityCodes;
	}
	
	public List<String> generateSecurityCodesWithNum(String codeSegment,Integer quantity){
		Set<String> securityCodes=new HashSet<String>();
		String[] codeGroup=codeSegment.split(systemParameterDAO.getSecurityCodeDelimiter());
		for(String group:codeGroup){
			SecurityCodeSegmentWithNum segment=new SecurityCodeSegmentWithNum(quantity,systemParameterDAO.getPrefixOfSecurityCode(),group);
			securityCodes.addAll(segment.generateCodes());
		}
		return securityCodes.stream().collect(Collectors.toList());
	}
	
	private void validateCode(User user,List<String> segmentCodes){
		Agent owner=agentDAO.findById(user.getId());
		List<SecurityCode> existingCode=securityCodeDAO.findByProperty("agentCode", owner.getAgentCode());
		if(Objects.isNull(existingCode)||existingCode.size()<1){
			throw new ApplicationException("录入者不持有任何防伪码商品");
		}
		List<String> ownerCodes=existingCode.stream().map((s)-> s.getCode()).collect(Collectors.toList());
		if(!ownerCodes.containsAll(segmentCodes)){
			throw new ApplicationException("录入了不是自己持有的防伪码商品");
		}
	}
	
	private Agent validateAgent(SecurityCode securityCode){
		if(!goodsIsExists(securityCode.getGoodsName())){
			throw new ApplicationException("所录入的商品名称不存在");
		}
		Agent agent=agentDAO.findByAgentCode(securityCode.getAgentCode());
		if(Objects.isNull(agent)){
			throw new ApplicationException("代理编码"+securityCode.getAgentCode()+"对应的代理信息不存在");
		}
		if(!Objects.equals(agent.getRealName(),securityCode.getOwner())){
			throw new ApplicationException("代理编码和姓名不匹配");
		}
		return agent;
	}
	public List<String> splitCodeSegment(String[] segment){
		List<String> securityCodes=new ArrayList<String>();
		return securityCodes;
	}
	
	private boolean goodsIsExists(String goodsName){
		if(Objects.isNull(goodsName)||goodsName.trim().isEmpty()){
			return false;
		}
		List<Goods> goodses=goodsDAO.findByName(goodsName);
		return Objects.nonNull(goodses) && goodses.size()>0;
	}
	
	@Transactional
	public void remove(SecurityCode code){
		LOG.warn("删除防伪码 {}",code.getCode());
		securityCodeDAO.delete(code);
	}
	
	@Transactional
	public void removeRange(SecurityCode code,String segmentCode){
		LOG.warn("删除防伪码 {} - {}",segmentCode);
		Agent agent=agentDAO.findByAgentCode(code.getAgentCode());
		if(Objects.isNull(agent)){
			throw new ApplicationException("代理编码"+code.getAgentCode()+"对应的代理信息不存在");
		}
		String[] codeGroup=segmentCode.split(systemParameterDAO.getSecurityCodeDelimiter());
		for(String group:codeGroup){
			String[] segment=group.split("-");
			if(segment.length>2){
				throw new ApplicationException("输入格式不合法 ");
			}
			String start=segment[0];
			String end=segment[0];
			if(segment.length>1){
				end=segment[1];
			}
			securityCodeDAO.deleteRange(code, start, end);
		}
		
		
	}
	
	@Transactional
	public void removeRangeWithNum(SecurityCode code,String segmentCode,Integer quantity){
		LOG.warn("删除防伪码 {} - {}",segmentCode);
		Agent agent=agentDAO.findByAgentCode(code.getAgentCode());
		if(Objects.isNull(agent)){
			throw new ApplicationException("代理编码"+code.getAgentCode()+"对应的代理信息不存在");
		}
		String[] codeGroup=segmentCode.split(systemParameterDAO.getSecurityCodeDelimiter());
		for(String group:codeGroup){
			String start=group,end=group;
			Integer endSeg=0;
			for(int index=1;index<quantity;index++){
				endSeg=index;
			}
			end=String.valueOf(Integer.parseInt(end)+endSeg);
			securityCodeDAO.deleteRange(code, start, end);
		}
		
		
	}
	
	@Autowired
	private SecurityCodeDAO securityCodeDAO;
	@Autowired
	private SystemParameterDAOInf systemParameterDAO;
	@Autowired
	private GoodsDAO goodsDAO;
	@Autowired
	private AgentDAO agentDAO;
	
	private final Logger LOG=LoggerFactory.getLogger(getClass());
}
