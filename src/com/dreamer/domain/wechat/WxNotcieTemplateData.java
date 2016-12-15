package com.dreamer.domain.wechat;

/**
 * Created by huangfei on 16/7/18.
 * 微信模版通知的单个消息类
 */
public class WxNotcieTemplateData {

    private String value;//通知内容
    private String color;//显示颜色
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}

