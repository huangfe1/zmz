package com.dreamer.domain.user;

import ps.mx.otter.utils.date.DateUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class AdvanceTransfer implements java.io.Serializable {

	private static final long serialVersionUID = 2759558931721988426L;
	// Fields

	private Integer id;
	private Integer version;
	private User userByToAgent;
	private User userByFromAgent;
	private Date transferTime;
	private Date updateTime;
	private String remark;
	private Double advance;
	private Double useVoucher;
	private AdvanceTransferType type;
    private String out_trade_no;//订单号码

	// Constructors


	public void transferAdvance(Agent from,Agent to,Double advance){
		userByToAgent=to;
		userByFromAgent=from;
		transferTime=new Date();
		this.advance=advance;
		type=AdvanceTransferType.NORMAL;
		from.transferAdvance(to,advance);//转移预存款
	}

	/** default constructor */
	public AdvanceTransfer() {
	}

	/** minimal constructor */
	public AdvanceTransfer(User userByToAgent, User userByFromAgent, Double advance) {
		this.userByToAgent = userByToAgent;
		this.userByFromAgent = userByFromAgent;
		this.advance = advance;
	}

	/** full constructor */
	public AdvanceTransfer(User userByToAgent, User userByFromAgent,
						   Date transferTime, Date updateTime, String remark,
						   Double advance) {
		this.userByToAgent = userByToAgent;
		this.userByFromAgent = userByFromAgent;
		this.transferTime = transferTime;
		this.updateTime = updateTime;
		this.remark = remark;
		this.advance = advance;
	}

    /**
     * 在线充值  提交余存款订单
     * @param userByFromAgent
     * @param userByToAgent
     */
    public void commit(User userByFromAgent,User userByToAgent){
        setOut_trade_no(fillOrderNo());//设置订单
        setUpdateTime(new Timestamp(System.currentTimeMillis()));//设置提交时间
        setUserByFromAgent(userByFromAgent);//假定为公司
        setUserByToAgent(userByToAgent);//收预存款的人
		setType(AdvanceTransferType.ERROR);//没有付款
    }

    /**
     * 全部用代金券支付
     * @param time
     */
	public void payForAdvanceByVoucher(String time){
        setTransferTime(DateUtil.string2date(time, "yyyyMMddHHmmss"));
        setType(AdvanceTransferType.PAY);
        setRemark(AdvanceTransferType.PAY.desc+"使用了代金券"+getUseVoucher());
        getUserByToAgent().getAccounts().deductVoucher(getUseVoucher(),"充值预存款扣减,订单"+getOut_trade_no());
        getUserByToAgent().getAccounts().increaseAdvance(getAdvance(),"在线充值,订单"+getOut_trade_no()+"使用代金券"+getUseVoucher());
	}

    public void payForAdvance(String time){
        setTransferTime(DateUtil.string2date(time, "yyyyMMddHHmmss"));
        setType(AdvanceTransferType.PAY);
        setRemark(VoucherTransferType.PAY.desc);
        if(getUseVoucher()>0){//有代金券可用
            getUserByToAgent().getAccounts().deductVoucher(getUseVoucher(),"充值预存款,订单"+getOut_trade_no());
        }
        getUserByToAgent().getAccounts().increaseAdvance(getAdvance(),"在线充值,订单"+getOut_trade_no());
    }

    public String fillOrderNo(){
        return Integer.toHexString(Objects.hashCode(DateUtil.date2string(Date.from(Instant.now()),"MMddHHmmss")));
    }
	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public AdvanceTransferType getType() {
		return type;
	}

	public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public void setVersion(Integer version) {
		this.version = version;
	}

	public User getUserByToAgent() {
		return this.userByToAgent;
	}

	public void setUserByToAgent(User userByToAgent) {
		this.userByToAgent = userByToAgent;
	}

	public User getUserByFromAgent() {
		return this.userByFromAgent;
	}

	public void setUserByFromAgent(User userByFromAgent) {
		this.userByFromAgent = userByFromAgent;
	}

	public Date getTransferTime() {
		return this.transferTime;
	}

	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return this.remark;
	}

	public Double getUseVoucher() {
		return useVoucher;
	}

	public void setUseVoucher(Double useVoucher) {
		this.useVoucher = useVoucher;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getAdvance() {
		return advance;
	}

	public void setType(AdvanceTransferType type) {
		this.type = type;
	}

	public void setAdvance(Double advance) {
		this.advance = advance;
	}
}