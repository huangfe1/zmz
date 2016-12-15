package com.dreamer.domain.account;

import com.dreamer.domain.user.Agent;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huangfei on 16/4/11.
 */
public class AdvanceRecord implements Serializable{

    private Integer id;

    private Integer type;// 0代表扣除  1代表增加

    private Double advance;//流动数量

    private Double advance_now;//变更后的量

    private String more;//详情

    private Agent agent;//所属人

    private Date updateTime;//产生时间


    public AdvanceRecord(Integer type,Agent agent,Double advance,String more,Double advance_now,Date updateTime){
        this.type=type;
        this.agent=agent;
        this.advance=advance;
        this.more=more;
        this.advance_now=advance_now;
        this.updateTime=updateTime;
    }
    public  AdvanceRecord(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getAdvance_now() {
        return advance_now;
    }

    public void setAdvance_now(Double advance_now) {
        this.advance_now = advance_now;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
