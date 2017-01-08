package com.dreamer.domain.mall.goods;

import com.dreamer.domain.user.Enum;

/**
 * Created by huangfei on 2017/1/7.
 */
public enum  MallGoodsType implements Enum{
    pmall("积分商城"),tc_pmall("特产商城");

    private String desc;

    MallGoodsType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
