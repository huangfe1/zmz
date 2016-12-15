package com.dreamer.view.mall.goods;

import java.io.Serializable;
import java.util.List;

public class GoodsDTO implements Serializable{

	private static final long serialVersionUID = -2711176171496975540L;

	private Integer id;

	private String name;

	private String spec;

	private Double price;

	private String imgUrl;

	private List<String> detailImgUrls;//详情页

	private String levelName;

	private Integer notLevel;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public Integer getNotLevel() {
		return notLevel;
	}
	public void setNotLevel(Integer notLevel) {
		this.notLevel = notLevel;
	}

	public List<String> getDetailImgUrls() {
		return detailImgUrls;
	}

	public void setDetailImgUrls(List<String> detailImgUrls) {
		this.detailImgUrls = detailImgUrls;
	}
}
