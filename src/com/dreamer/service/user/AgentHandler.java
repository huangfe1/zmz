package com.dreamer.service.user;

import com.dreamer.domain.authorization.Authorization;
import com.dreamer.domain.user.*;

public interface AgentHandler {

	public abstract Agent selfRegister(Agent agent, String referrerAgentCode);

	public abstract void addAgent(User operator, Agent agent, Integer[] ids, Agent parent,Agent teqparent);

	public abstract boolean agentDuplicate(Agent agent);

	public abstract void transfer(Agent fromAgent, Agent toAgent, User operator);

	public abstract void removeAuthorization(User operator, Agent agent,
			Authorization authorization);

	public abstract void updateAgent(User operator, Agent agent, Integer[] ids, Agent parent,Agent teqparent);

	public abstract void auditAgent(User operator, Agent agent, Integer[] ids);

	public abstract void activeAgent(User operator, Agent agent);
	
	public abstract void setWxOpenIdTo(Agent agent,String openId);

	public abstract void activeSingleAuthorization(User operator, Agent agent,
			Authorization auth);

	public abstract void georganizeAgent(User operator, Agent agent);

	public abstract void removeAgent(User operator, Agent agent);

	public abstract void removeVoucher(User operator, VoucherTransfer voucherTransfer);

	public abstract void transferPoints(User operator, PointsTransfer transfer,
			String toAgentCode, String toAgentName, Double transferPoints);
	
	public abstract void transferVoucher(User operator, VoucherTransfer transfer,
			String toAgentCode, String toAgentName, Double transferVoucher);

	public abstract void addVoucher(User operator, VoucherTransfer transfer);



	public abstract void addAdvance(User operator, AdvanceTransfer transfer);

    public abstract void payForVoucher(String time,VoucherTransfer transfer);

    public abstract void payForAdvanceByVoucher(String time,AdvanceTransfer transfer);

    public abstract void payForAdvance(String time,AdvanceTransfer transfer);


	public abstract void mergeAdvance(AdvanceTransfer transfer);

	public abstract void mergeVoucher(VoucherTransfer transfer);



	public abstract Agent findAgentById(Integer id);

    public abstract void changePriceLevel(Agent agent,Integer goodsId,Integer levelId);
	
	public abstract void batchGenerateSubdomain();

}