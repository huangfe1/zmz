package com.dreamer.domain.authorization;

import java.util.Date;
import java.util.Objects;

import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.user.Agent;
import com.dreamer.domain.user.AgentStatus;

public class Authorization implements java.io.Serializable {

	private static final long serialVersionUID = 5349802237770398637L;
	private Integer id;
	private Agent agent;
	private AuthorizationType authorizationType;//授权类型
	private AgentStatus status;//授权状态
	private Date updateTime;
	private Integer version;
	
	public boolean isMainAuthorization(){
		return authorizationType.isMainAuthorization();
	}
	
	public Goods getAuthorizedGoods(){
		return authorizationType.getGoods();
	}
	
	public boolean isAuthorizedGoods(Goods goods){
		return getAuthorizedGoods().equals(goods);
	}
	
	public void active(){
		status=AgentStatus.ACTIVE;
		if(Objects.nonNull(agent)){
			agent.active();
		}
	}
	
	public boolean isActive(){
		return status!=null && status==AgentStatus.ACTIVE;
	}
	
	public void inactive(){
		status=AgentStatus.NO_ACTIVE;
		if(Objects.nonNull(agent)){
			if(agent.getAuthorizations().stream().filter((auth)->!auth.isInactive()).count()==0){
				agent.inactive();
			}
		}
	}
	
	public boolean isInactive(){
		return status!=null && status==AgentStatus.NO_ACTIVE;
	}
	
	public void reorganize(){
		status=AgentStatus.REORGANIZE;
		if(Objects.nonNull(agent)){
			if(agent.getAuthorizations().stream().filter((auth)->!auth.isReorganize()).count()==0){
				agent.reorganize();
			}
		}
	}
	
	public boolean isReorganize(){
		return status!=null && status==AgentStatus.REORGANIZE;
	}

	// Constructors

	/** default constructor */
	public Authorization() {
	}

	/** minimal constructor */
	public Authorization(Agent user, AuthorizationType authorizationType) {
		this.agent = user;
		this.authorizationType = authorizationType;
	}

	/** full constructor */
	public Authorization(Agent user, AuthorizationType authorizationType,
			AgentStatus status, Date updateTime, Integer version) {
		this.agent = user;
		this.authorizationType = authorizationType;
		this.status = status;
		this.updateTime = updateTime;
		this.version = version;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent user) {
		this.agent = user;
	}

	public AuthorizationType getAuthorizationType() {
		return this.authorizationType;
	}

	public void setAuthorizationType(AuthorizationType authorizationType) {
		this.authorizationType = authorizationType;
	}

	public AgentStatus getStatus() {
		return this.status;
	}

	public void setStatus(AgentStatus status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(agent,authorizationType);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Authorization)) {
			return false;
		}
		Authorization other = (Authorization) object;
		if(Objects.equals(agent, other.getAgent()) && Objects.equals(authorizationType, other.getAuthorizationType())){
			return true;
		}else{
			return false;
		}
	}

}