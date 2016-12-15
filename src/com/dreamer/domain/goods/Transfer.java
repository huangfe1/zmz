package com.dreamer.domain.goods;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.exception.DataNotFoundException;

import com.dreamer.domain.account.GoodsAccount;
import com.dreamer.domain.user.Accounts;
import com.dreamer.domain.user.Agent;

/**
 * 转货类
 * 
 * @author Tankman
 *
 */

public class Transfer implements java.io.Serializable {

	private static final long serialVersionUID = -5140495984534174981L;
	private Integer id;
	private Integer version;
	private Agent userByToAgent;
	private Agent userByFromAgent;
	private GoodsTransferStatus status;
	private Timestamp applyTime;
	private Timestamp transferTime;
	private Timestamp updateTime;
	private String remittance;
	private String remark;
	private Double points;
	private Double voucher;
    private Double advance;
	private Integer quantity;
	private Map<Integer,TransferItem> items=new HashMap<Integer,TransferItem>();
	private Boolean back;
	private Boolean useVoucher;//是否使用代金券
	private TransferApplyOrigin applyOrigin;//转货订单类型

	public Boolean getUseVoucher() {
		return useVoucher;
	}

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public void setUseVoucher(Boolean useVoucher) {
		this.useVoucher = useVoucher;
	}

	public Boolean getBack() {
		return back;
	}

	public void setBack(Boolean back) {
		this.back = back;
	}

	public void addTransferItem(Goods goods,Integer quantity,Price price){
		TransferItem item=TransferItem.build(goods, quantity, price);
		addItem(item);
	}
	
	public void addBackTransferItem(Goods goods,Integer quantity,Price price){
		TransferItem item=TransferItem.build(goods, -quantity, price);
		addItem(item);
	}
	
	public void refuse(){
		status=GoodsTransferStatus.DISAGREE;
	}
	
	public void apply(){
		applyTime=new Timestamp(System.currentTimeMillis());
		status=GoodsTransferStatus.NEW;
		calculate();
	}

    /**
     *退货申请
     */
    public void backApply(){
        applyTime=new Timestamp(System.currentTimeMillis());
        status=GoodsTransferStatus.NEW;
        calculate();
    }




	public void calculate(){
		points=caculatePoints();
		quantity=caculateQuantity();
        if(getUseVoucher()!=null&&getUseVoucher()) {//使用代金券
            voucher = voucherAmountPayable();
        }else {
            voucher=0.0;
        }
	}



	/**
	 * 代金券可支付金额
	 * @return
	 */
	public Double voucherAmountPayable(){
		Double voucherBalance=userByToAgent.getAccounts().getVoucherBalance();
		Double amount=getAmount();
		return amount>=voucherBalance?voucherBalance:amount;
	}

    /**
     * 预存款可支付金额
     * @return
     */
    public Double advanceAmountPayable(){
        Double advanceBalance=userByToAgent.getAccounts().getAdvanceBalance();
        Double amount=getAmount();
        return amount>=advanceBalance?advanceBalance:amount;
    }
	
	private double caculatePoints(){
		return items.values().stream().mapToDouble(item->item.getPoints()).sum();
	}
	
	private Integer caculateQuantity(){
		return items.values().stream().mapToInt(item->item.getQuantity()).sum();
	}
	
	
	
	public void confirm(){
		transferTime=new Timestamp(System.currentTimeMillis());
		status=GoodsTransferStatus.CONFIRM;
		points=caculatePoints();
		quantity=caculateQuantity();
		transfer();
	}

	public void confirmAutoByAdvance(){

        if(getUseVoucher()!=null&&getUseVoucher()){//使用代金券
            //余额不足
            if(advanceAmountPayable()+voucherAmountPayable()<getAmount()){
                throw new ApplicationException("余额不足,请充值!");
            }
        }else{
            if(advanceAmountPayable()<getAmount()){
                throw new ApplicationException("余额不足,请充值!");
            }
        }
        setAdvance(advanceAmountPayable());//扣减可用的预存款
		//先扣减预存款
        userByToAgent.getAccounts().payAdvanceTo(userByFromAgent.getAccounts(), advanceAmountPayable());
        //支付代金券
        BigDecimal b1= BigDecimal.valueOf(getAmount());
        BigDecimal b2= BigDecimal.valueOf(getAdvance());
        setVoucher(b1.subtract(b2).doubleValue());
        confirm();//已经扣减代金券
	}

    /**
     * 主动将货物转出
     * @param toAgent
     */
	public void transferToAnother(){
		status=GoodsTransferStatus.CONFIRM;
		back=false;
		setApplyTime(new Timestamp(System.currentTimeMillis()));
		transferTime=new Timestamp(System.currentTimeMillis());
        if(voucher==null)
		voucher=0D;
        transfer();
//		if(userByFromAgent.isMyParent(toAgent)){
////			setRemittance("转货退回");
//			transferToBack();
//		}else{
////			setRemittance("主动转出");
//			transfer();
//		}
	}

	/**
	 * 将货物退给上级 add By hf
	 */
	public void transferBackToParent(){
		this.setBack(true);
		status=GoodsTransferStatus.CONFIRM;
		setApplyTime(new Timestamp(System.currentTimeMillis()));
		transferTime=new Timestamp(System.currentTimeMillis());
		voucher=0D;
        this.getItems().forEach((k,v)->{
            GoodsAccount accTo = userByToAgent.loadAccountForGoodsId(k);
            if (Objects.isNull(accTo)) {
                throw new DataNotFoundException("转入方对应货物账户不存在");
            }
            GoodsAccount accFrom = userByFromAgent.loadAccountForGoodsId(k);
            if (Objects.isNull(accFrom)) {
                throw new DataNotFoundException("转出方对应货物账户不存在");
            }
            accFrom.transferGoodsToBack(accFrom,accTo, v.getQuantity());
        });
        String more="退货返还代金券给"+userByFromAgent.getRealName();
        userByToAgent.getAccounts().payVoucherTo(userByFromAgent.getAccounts(),getAmount(),more);
	}
	
//	public void transferBackToParent(Agent toAgent){
//		this.setBack(true);
//		status=GoodsTransferStatus.CONFIRM;
//		userByToAgent=toAgent;
//		setApplyTime(new Timestamp(System.currentTimeMillis()));
//		transferTime=new Timestamp(System.currentTimeMillis());
//		voucher=0D;
////        setRemittance("转货退回");
//		transferToBack();
////		if(userByFromAgent.isMyParent(toAgent)){
////			setRemittance("转货退回");
////			transferToBack();
////		}else{
////			throw new ApplicationException("只能向上级退货");
////		}
//	}
	

	private void transfer(){
		points=caculatePoints();
		userByFromAgent.transferGoodsToAnother(userByToAgent, this);
		if(getVoucher()>0){
			Accounts fromAccounts=userByFromAgent.getAccounts();
			if(Objects.isNull(fromAccounts)){
				fromAccounts=userByFromAgent.generateAccounts();
			}
            if(getUseVoucher()!=null&&getUseVoucher())//如果使用代金券
            {
                userByToAgent.getAccounts().payVoucherTo(fromAccounts, getVoucher());

            }
		}
	}
	
	public Double getAmount(){
		BigDecimal p=new BigDecimal(items.values().stream().mapToDouble(item->item.getAmount()).sum());
		return p.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public boolean isNew(){
		return status==GoodsTransferStatus.NEW;
	}

	// Constructors

	/** default constructor */
	public Transfer() {
	}

	/** minimal constructor */
	public Transfer(Agent userByToAgent, Agent userByFromAgent,
			GoodsTransferStatus status, Timestamp applyTime, String remittance) {
		this.userByToAgent = userByToAgent;
		this.userByFromAgent = userByFromAgent;
		this.status = status;
		this.applyTime = applyTime;
		this.remittance = remittance;
	}

	/** full constructor */
	public Transfer(Agent userByToAgent, Agent userByFromAgent,
			GoodsTransferStatus status, Timestamp applyTime,
			Timestamp transferTime, Timestamp updateTime, String remittance,
			String remark, Double point) {
		this.userByToAgent = userByToAgent;
		this.userByFromAgent = userByFromAgent;
		this.status = status;
		this.applyTime = applyTime;
		this.transferTime = transferTime;
		this.updateTime = updateTime;
		this.remittance = remittance;
		this.remark = remark;
		this.points = point;
	}
/**
 * hf 是否是官方商城申请的
 * @return 
 */
	public boolean isGmallApply(){
		return applyOrigin==TransferApplyOrigin.GMALL;
	}
	/**
	 * hf 是否是普通商城的
	 * @return
	 */
	public boolean isMallApply(){
		return applyOrigin==TransferApplyOrigin.MALL;
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

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Agent getUserByToAgent() {
		return this.userByToAgent;
	}

	public void setUserByToAgent(Agent userByToAgent) {
		this.userByToAgent = userByToAgent;
	}

	public Agent getUserByFromAgent() {
		return this.userByFromAgent;
	}

	public void setUserByFromAgent(Agent userByFromAgent) {
		this.userByFromAgent = userByFromAgent;
	}

	public Integer getQuantity() {
		quantity= caculateQuantity();
		return quantity;
	}

	public GoodsTransferStatus getStatus() {
		return this.status;
	}

	public void setStatus(GoodsTransferStatus status) {
		this.status = status;
	}

	public Timestamp getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public Timestamp getTransferTime() {
		return this.transferTime;
	}

	public void setTransferTime(Timestamp transferTime) {
		this.transferTime = transferTime;
	}

	public Timestamp getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemittance() {
		return this.remittance;
	}

	public void setRemittance(String remittance) {
		this.remittance = remittance;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getPoints() {
		return this.points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Double getVoucher() {
		return voucher;
	}

	public void setVoucher(Double voucher) {
		this.voucher = voucher;
	}
	
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Map<Integer, TransferItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, TransferItem> items) {
		this.items = items;
	}
	
	public void addItem(TransferItem item){
		item.setTransfer(this);
		items.put(item.getGoodsId(), item);
	}

    /**
     * 退货
     * @return
     */
	public boolean isBackedTransfer() {
		if(applyOrigin==TransferApplyOrigin.BACK){
			return true;
		}
		return false;
	}

    /**
     * 主动转出
     * @return
     */
    public boolean isOutTransfer() {
        if(applyOrigin==TransferApplyOrigin.OUT){
            return true;
        }
        return false;
    }

	public void clear(){
		getItems().clear();
	}

	public TransferApplyOrigin getApplyOrigin() {
		return applyOrigin;
	}

	public void setApplyOrigin(TransferApplyOrigin applyOrigin) {
		this.applyOrigin = applyOrigin;
	}

	
	
	
}