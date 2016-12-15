package com.dreamer.domain.mall.goods;

import com.fasterxml.jackson.annotation.JsonView;
import ps.mx.otter.exception.ApplicationException;
import ps.mx.otter.utils.json.BaseView;

import java.sql.Timestamp;
import java.util.Date;

public class MallGoods implements java.io.Serializable {

	private static final long serialVersionUID = -6000613049374262520L;
	@JsonView(MallGoodsView.class)
	private Integer id;
	@JsonView(MallGoodsView.class)
	private Integer version;
	@JsonView({MallGoodsView.class,BaseView.class})
	private String name;
	@JsonView({MallGoodsView.class,BaseView.class})
	private String shareName;

	@JsonView({MallGoodsView.class,BaseView.class})
	private String shareDetail;

    @JsonView({MallGoodsView.class,BaseView.class})
    private Double profit;//利润

	@JsonView(MallGoodsView.class)
	private String spec;
	@JsonView(MallGoodsView.class)
	private Boolean shelf;
	@JsonView(MallGoodsView.class)
	private Date updateTime;
	@JsonView(MallGoodsView.class)
	private Double voucher;
	@JsonView(MallGoodsView.class)
	private Integer sequence;
	@JsonView(MallGoodsView.class)
	private Double price;
	@JsonView(MallGoodsView.class)
	private Double pointPrice;
	@JsonView(MallGoodsView.class)
	private Double moneyPrice;
	@JsonView(MallGoodsView.class)
	private String imgFile;
	@JsonView(MallGoodsView.class)
	private Double benefitPoints;
	@JsonView(MallGoodsView.class)
	private Integer stockQuantity;
	@JsonView(MallGoodsView.class)
	private String vouchers;
	@JsonView(MallGoodsView.class)
	private String detailImg;//详情页名字
	private Integer activity;//是否活动,0代表不在活动,1代表活动
	private Timestamp startTime;//活动开始时间
    private Integer limitNumer;//活动限购数量




    public void addStockBlotter(MallGoodsStockBlotter stock){
		stock.setGoods(this);
		stockValidate(stock);
		increaseCurrentStock(stock.getChange());
		stock.recordChange(this);
	}
	
	public Integer increaseCurrentStock(Integer added){
		if(added>0){
			stockQuantity=(stockQuantity+added);
		}
		return stockQuantity;
	}
	
	public Integer deductCurrentStock(Integer deduct){
		if(deduct>stockQuantity){
			throw new ApplicationException("货物总库存不足");
		}
		stockQuantity=(stockQuantity-deduct);
		return stockQuantity;
	}
	
	public void stockValidate(MallGoodsStockBlotter stock){
		if(stock.getChange()<0){
			if(stockQuantity+stock.getChange()<0){
				throw new ApplicationException("库存变更为减少时,减少值不能大于现有库存");
			}
		}
	}
	
	public Double caculateBenefitPoints(Integer quantity){
		return benefitPoints*quantity;
	}
	
	public Double caculateVoucher(Integer quantity){
		return voucher*quantity;
	}
	// Constructors

	/** default constructor */
	public MallGoods() {
	}

	/** minimal constructor */
	public MallGoods(String name, Boolean shelf, Double voucher, Double price) {
		this.name = name;
		this.shelf = shelf;
		this.voucher = voucher;
		this.price = price;
	}

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    /** full constructor */
	public MallGoods(String name, String spec, Boolean shelf,
			Date updateTime, Double voucher, Integer sequence,
			Double price, Double pointPrice, Double moneyPrice,String vouchers) {
		this.name = name;
		this.spec = spec;
		this.shelf = shelf;
		this.updateTime = updateTime;
		this.voucher = voucher;
		this.sequence = sequence;
		this.price = price;
		this.pointPrice = pointPrice;
		this.moneyPrice = moneyPrice;
        this.vouchers=vouchers;
	}

	public String getDetailImg() {
		return detailImg;
	}

	public void setDetailImg(String detailImg) {
		this.detailImg = detailImg;
	}

// Property accessors


	public Integer getActivity() {
		return activity;
	}

	public void setActivity(Integer activity) {
		this.activity = activity;
	}

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Integer getLimitNumer() {
        return limitNumer;
    }

    public void setLimitNumer(Integer limitNumer) {
        this.limitNumer = limitNumer;
    }

    public String getVouchers() {
        return vouchers;
    }

    public void setVouchers(String vouchers) {
        this.vouchers = vouchers;
    }

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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Boolean getShelf() {
		return this.shelf;
	}

	public void setShelf(Boolean shelf) {
		this.shelf = shelf;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Double getVoucher() {
		return this.voucher;
	}

	public void setVoucher(Double voucher) {
		this.voucher = voucher;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPointPrice() {
		return this.pointPrice;
	}

	public void setPointPrice(Double pointPrice) {
		this.pointPrice = pointPrice;
	}

	public Double getMoneyPrice() {
		return this.moneyPrice;
	}

	public void setMoneyPrice(Double moneyPrice) {
		this.moneyPrice = moneyPrice;
	}

	public String getImgFile() {
		return imgFile;
	}

	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}
	
	public Double getBenefitPoints() {
		return benefitPoints;
	}

	public void setBenefitPoints(Double benefitPoints) {
		this.benefitPoints = benefitPoints;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	public String getShareDetail() {
		return shareDetail;
	}

	public void setShareDetail(String shareDetail) {
		this.shareDetail = shareDetail;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public interface MallGoodsView extends BaseView{}

}