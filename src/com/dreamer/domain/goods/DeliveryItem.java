package com.dreamer.domain.goods;

import java.math.BigDecimal;
import java.util.Objects;

public class DeliveryItem implements java.io.Serializable {

	private static final long serialVersionUID = -6328294535581153030L;
	private Integer id;
	private Integer version;
	private Goods goods;
	private DeliveryNote deliveryNote;
	private Integer quantity;
	private Double point;
	private Double price;

	
	public Double caculatePoints(){
		setPoint(goods.caculatePoints(quantity));
		return getPoint();
	}
	
	public Double getAmount(){
		if(Objects.isNull(price)){
			price=0d;
		}
		BigDecimal p=new BigDecimal(String.valueOf(price*quantity));
		return p.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	public boolean stockEnough(){
		return goods.stockEnough(quantity);
	}
	// Constructors

	/** default constructor */
	public DeliveryItem() {
	}

	/** minimal constructor */
	public DeliveryItem(Goods goods, Integer quantity) {
		this.goods = goods;
		this.quantity = quantity;
	}
	
	/** minimal constructor */
	public DeliveryItem(Goods goods, Integer quantity,Double price) {
		this.goods = goods;
		this.quantity = quantity;
		this.price=price;
	}

	/** full constructor */
	public DeliveryItem(Goods goods, DeliveryNote deliveryNote,
			Integer quantity, Double point) {
		this.goods = goods;
		this.deliveryNote = deliveryNote;
		this.quantity = quantity;
		this.point = point;
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

	public Goods getGoods() {
		return this.goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public DeliveryNote getDeliveryNote() {
		return this.deliveryNote;
	}

	public void setDeliveryNote(DeliveryNote deliveryNote) {
		this.deliveryNote = deliveryNote;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPoint() {
		return this.point;
	}

	public void setPoint(Double point) {
		this.point = point;
	}
	
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public int hashCode(){
		return Objects.hash(goods);
	}
	
	@Override
	public boolean equals(Object object){
		if(Objects.isNull(object)){
			return false;
		}
		if(!(object instanceof DeliveryItem)){
			return false;
		}
		DeliveryItem another=(DeliveryItem)object;
		return Objects.equals(goods, another.getGoods());
	}

}