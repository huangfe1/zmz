package com.dreamer.domain.goods;

/**
 * Created by huangfei on 16/3/24.
 */
public class Logistics {

    private String provinces;//身份

    private String weights;//重量区间

    private String prices;//价额区间

    private Integer id;

    public String getProvinces() {
        return provinces;
    }

    public void setProvinces(String provinces) {
        this.provinces = provinces;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getPrices() {
        return prices;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }
}
