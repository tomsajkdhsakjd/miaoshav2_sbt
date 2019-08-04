package com.shendehai.com.service;


import com.shendehai.com.common.entity.Result;
import com.shendehai.com.common.entity.Seckill;

import java.util.List;

public interface SeckillService {
    /**
     * 查询全部的秒杀记录
     *
     * @return
     */
    List<Seckill> getSeckillList();

    Seckill getSeckillByid(long id);

    Result startSeckil(long killId, long userId);


    String MD5Url(long seckillId);

    Result excuteSeckill(Long seckillId, String phone);


}
