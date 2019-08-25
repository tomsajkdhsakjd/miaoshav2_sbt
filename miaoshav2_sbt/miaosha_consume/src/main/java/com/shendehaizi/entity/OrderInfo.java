package com.shendehaizi.entity;

import lombok.Data;

import java.util.Date;

public class OrderInfo {
    private Integer order_id;
    private String seckill_name;
    private String usr_phone;
    private Date create_time;
    private Integer seckild_id;
    private  Integer state;

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getSeckill_name() {
        return seckill_name;
    }

    public void setSeckill_name(String seckill_name) {
        this.seckill_name = seckill_name;
    }

    public String getUsr_phone() {
        return usr_phone;
    }

    public void setUsr_phone(String usr_phone) {
        this.usr_phone = usr_phone;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getSeckild_id() {
        return seckild_id;
    }

    public void setSeckild_id(Integer seckild_id) {
        this.seckild_id = seckild_id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "order_id=" + order_id +
                ", seckill_name='" + seckill_name + '\'' +
                ", usr_phone='" + usr_phone + '\'' +
                ", create_time=" + create_time +
                ", seckild_id=" + seckild_id +
                ", state=" + state +
                '}';
    }
}
