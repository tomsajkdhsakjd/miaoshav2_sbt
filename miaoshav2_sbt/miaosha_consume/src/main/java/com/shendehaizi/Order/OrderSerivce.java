package com.shendehaizi.Order;

import com.shendehaizi.Redis.RedisUtil;
import com.shendehaizi.mapper.SeckillDao;
import com.shendehaizi.mapper.SucessSeckillDao;
import entity.SeckillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSerivce {
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SucessSeckillDao sucessSeckillDao;

    public void excultdelseckill(SeckillMessage message) {
        String seckillId = message.getSeckillId();
        String phone = message.getPhone();
        int i = sucessSeckillDao.insertSucessKilled(Long.valueOf(seckillId), Long.valueOf(phone));
        if(i<=0){
            redisUtil.setnx(phone,"重复秒杀!");
        }else {
            int integer = seckillDao.delSeckillone(Long.valueOf(seckillId));
            if (integer <= 0) {
                redisUtil.setnx(phone, "抱歉秒杀商品已售完!");
            }else{
                redisUtil.set(phone,"秒杀成功");
            }
        }
    }
}
