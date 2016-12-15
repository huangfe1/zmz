package com.dreamer.domain.user;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import com.dreamer.domain.goods.GoodsType;
import ps.mx.otter.utils.json.BaseView;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.authorization.AuthorizationType;
import com.dreamer.domain.goods.Goods;
import com.dreamer.domain.goods.Transfer;
import com.dreamer.domain.system.Module;
import com.fasterxml.jackson.annotation.JsonView;

public abstract class User implements java.io.Serializable {

	private static final long serialVersionUID = -6857285918433321049L;
	@JsonView(UserBaseView.class)
	private Integer id;
	@JsonView(UserBaseView.class)
	private String realName;
	private String loginName;
	@JsonView(UserBaseView.class)
	private String mobile;
	@JsonView(UserBaseView.class)
	private String weixin;
	@JsonView(UserBaseView.class)
	private String idCard;
	private Agent parent;
	private Agent teqparent;//特权商城
	private Agent referrer;
	@JsonView(UserBaseView.class)
	private UserStatus userStatus;
	private String password;
	private Timestamp updateTime;
	private Integer version;
	private Integer operator;
	private String wxOpenid;
	private String nickName;
	private Integer identity;
	private Set<GoodsAccount> goodsAccounts = new HashSet<GoodsAccount>(0);
	private String lastVisitIP;
	private Timestamp lastVisitTime;
	private String subDomain;
	private Accounts accounts;
	private Date joinDate;
	private String loginToken;

	/**
	 * 代金券流动记录
	 */
	public abstract void addVoucherRecord(Integer type,String more,Double voucher);

	/**
	 * 预存款流动记录
	 */
	public abstract void addAdvanceRecord(Integer type,String more,Double advance);

	/**
	 * 从本帐户转货给其他账户
	 * 
	 * @param user
	 * @param goods
	 * @param amount
	 */
	public abstract void transferGoodsToAnother(Agent user, Transfer transfer);

	public abstract void addAuthorizationToAgent(Agent agent,
			List<AuthorizationType> types);

	public abstract List<Module> getLeafModules();

	public abstract Set<Module> getTopModules();

	/**
	 * 用户是否已拥有指定产品授权(拥有指定产品授权或者任一主产品授权)且授权激活
	 * @param goods 检查是否授权的产品
	 * @return false-未授权 true-已授权
	 */
	public boolean isActivedAuthorizedGoods(Goods goods) {
		return isMutedUser();
	}

	public boolean isMyParent(User agent){
		return Objects.equals(getParent(), agent);
	}
	
	public boolean notParentChildRelation(User agent){
		return !(this.isMyParent(agent) || agent.isMyParent(this));
	}
	
	public abstract boolean hasMainGoodsAuthorization(GoodsType gt);

	public boolean hasGoodsAccount(Goods goods) {
		Optional<GoodsAccount> optional = goodsAccounts.stream()
				.filter((g) -> Objects.equals(g.getGoods(), goods)).findFirst();
		return optional.isPresent();
	}

	
	public Double increasePoints(Double points) {
		return accounts.increasePoints(points);
	}

	public Double deductPoints(Double points) {
		return accounts.deductPoints(points);
	}

	public void addGoodsAccount(GoodsAccount accs) {
		if (goodsAccounts.add(accs)) {
			accs.setUser(this);
		}
	}

	public abstract boolean isAdmin();

	public abstract boolean isAgent();

	public abstract boolean isVisitor();

	public abstract boolean isMutedUser();

	public abstract boolean isSuper();

	public boolean isTopAgent() {
		return isAgent() && (Objects.nonNull(parent) && parent.isMutedUser());
	}

	/**
	 * 黄飞新增  是否是顶级代理
	 * @return
     */
	public boolean isTopTeqAgent() {
		return isAgent() && (Objects.nonNull(teqparent) && teqparent.isMutedUser());
	}

	public boolean isNew() {
		return UserStatus.NEW == userStatus;
	}

	public boolean isStoped() {
		return UserStatus.STOP == userStatus;
	}

	public boolean isLocked() {
		return UserStatus.LOCKED == userStatus;
	}

	public void lock() {
		if (isNormal()) {
			this.userStatus = UserStatus.LOCKED;
		}
	}

	public void unlock() {
		if (isLocked()) {
			this.userStatus = UserStatus.NORMAL;
		}
	}

	public boolean isNormal() {
		return !isLocked() && !isStoped();
	}

	public void normal() {
		if (!isNormal()) {
			this.userStatus = UserStatus.NORMAL;
		}
	}

	public String defaultPassword() {
		String idCard = getIdCard();
		if (idCard != null && idCard.length() > 5) {
			return idCard.substring(idCard.length() - 6, idCard.length());
		} else {
			return null;
		}
	}

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String realName, String loginName, String mobile,
			UserStatus userStatus) {
		this.realName = realName;
		this.loginName = loginName;
		this.mobile = mobile;
		this.userStatus = userStatus;
	}

	/** full constructor */
	public User(String realName, String loginName, String mobile,
			String weixin, String idCard, UserStatus userStatus,
			String password, Timestamp updateTime, Integer version,
			Integer operator, String wxOpenid, String nickName,
			Integer identity, Set<GoodsAccount> accounts) {
		this.realName = realName;
		this.loginName = loginName;
		this.mobile = mobile;
		this.weixin = weixin;
		this.idCard = idCard;
		this.userStatus = userStatus;
		this.password = password;
		this.updateTime = updateTime;
		this.version = version;
		this.operator = operator;
		this.wxOpenid = wxOpenid;
		this.nickName = nickName;
		this.identity = identity;
		this.goodsAccounts = accounts;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWeixin() {
		return this.weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public UserStatus getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getOperator() {
		return this.operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public String getWxOpenid() {
		return this.wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getIdentity() {
		return this.identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public Set<GoodsAccount> getGoodsAccounts() {
		return this.goodsAccounts;
	}

	public void setGoodsAccounts(Set<GoodsAccount> accounts) {
		this.goodsAccounts = accounts;
	}

	public Agent getParent() {
		return parent;
	}

	public void setParent(Agent parent) {
		this.parent = parent;
	}

	public String getLastVisitIP() {
		return lastVisitIP;
	}

	public void setLastVisitIP(String lastVisitIP) {
		this.lastVisitIP = lastVisitIP;
	}

	public Timestamp getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Timestamp lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public Accounts getAccounts() {
		return accounts;
	}

	public Agent getTeqparent() {
		return teqparent;
	}

	public void setTeqparent(Agent teqparent) {
		this.teqparent = teqparent;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

	public Agent getReferrer() {
		return referrer;
	}

	public void setReferrer(Agent referrer) {
		this.referrer = referrer;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public interface UserBaseView extends BaseView {
	}

	
	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	
	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return Objects.hash(id, loginName, idCard);
	}
	

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		return Objects.equals(id, other.getId())
				&& Objects.equals(loginName, other.getLoginName())
				&& Objects.equals(idCard, other.getIdCard());
	}

}