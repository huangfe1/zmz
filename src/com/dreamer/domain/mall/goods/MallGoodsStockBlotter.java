package com.dreamer.domain.mall.goods;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import ps.mx.otter.utils.json.DateTimeDeserializer;
import ps.mx.otter.utils.json.DateTimeSerializer;

import com.dreamer.domain.user.Admin;
import com.dreamer.domain.user.User.UserBaseView;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class MallGoodsStockBlotter implements java.io.Serializable {

	private static final long serialVersionUID = -1517102293182165804L;
	@JsonView(MallGoodsStockBlotterView.class)
	private Integer id;
	@JsonView(MallGoodsStockBlotterView.class)
	private Admin user;
	@JsonView(MallGoodsStockBlotterView.class)
	private MallGoods goods;
	@JsonView(MallGoodsStockBlotterView.class)
	private Integer change;
	@JsonView(MallGoodsStockBlotterView.class)
	private Integer currentBalance;
	@JsonView(MallGoodsStockBlotterView.class)
	@DateTimeFormat(iso=ISO.DATE_TIME)
	@JsonDeserialize(using=DateTimeDeserializer.class)
	@JsonSerialize(using=DateTimeSerializer.class)
	private Date operateTime;
	@JsonView(MallGoodsStockBlotterView.class)
	private Integer currentStock;
	@JsonView(MallGoodsStockBlotterView.class)
	private Double point;
	@JsonView(MallGoodsStockBlotterView.class)
	private Double currentPoint;
	@JsonView(MallGoodsStockBlotterView.class)
	private String remark;
	
	
	public void recordChange(MallGoods goods){
		setCurrentStock(goods.getStockQuantity());
		setOperateTime(new Date());
	}

	// Constructors

	/** default constructor */
	public MallGoodsStockBlotter() {
	}

	/** minimal constructor */
	public MallGoodsStockBlotter(MallGoods goods, Integer change) {
		this.goods = goods;
		this.change = change;
	}

	/** full constructor */
	public MallGoodsStockBlotter(Admin user, MallGoods goods, Integer change,
			Integer currentBalance, Date operateTime,
			Integer currentStock, Double point, Double currentPoint,
			String remark) {
		this.user = user;
		this.goods = goods;
		this.change = change;
		this.currentBalance = currentBalance;
		this.operateTime = operateTime;
		this.currentStock = currentStock;
		this.point = point;
		this.currentPoint = currentPoint;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Admin getUser() {
		return this.user;
	}

	public void setUser(Admin user) {
		this.user = user;
	}

	public MallGoods getGoods() {
		return this.goods;
	}

	public void setGoods(MallGoods goods) {
		this.goods = goods;
	}

	public Integer getChange() {
		return this.change;
	}

	public void setChange(Integer change) {
		this.change = change;
	}

	public Integer getCurrentBalance() {
		return this.currentBalance;
	}

	public void setCurrentBalance(Integer currentBalance) {
		this.currentBalance = currentBalance;
	}

	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getCurrentStock() {
		return this.currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public Double getPoint() {
		return this.point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}

	public Double getCurrentPoint() {
		return this.currentPoint;
	}

	public void setCurrentPoint(Double currentPoint) {
		this.currentPoint = currentPoint;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public interface MallGoodsStockBlotterView extends UserBaseView{}
}