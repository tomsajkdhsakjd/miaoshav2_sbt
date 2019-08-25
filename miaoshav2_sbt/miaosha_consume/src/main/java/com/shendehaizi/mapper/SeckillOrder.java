package com.shendehaizi.mapper;

import com.shendehaizi.entity.OrderInfo;

import java.util.List;

public interface SeckillOrder {
    void insertOrderdetail(OrderInfo orderInfo);
     List<OrderInfo> getnotpayorder();
    void delnotpayorder(Integer order_id);
}
