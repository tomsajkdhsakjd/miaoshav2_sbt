package com.shendehaizi.Order;

import com.shendehaizi.mapper.SeckillDao;
import entity.SeckillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSerivce {
    @Autowired
    private SeckillDao seckillDao;
    public void  excultdelseckill(SeckillMessage message){
      //  Integer integer = seckillDao.delSeckillone(seckildid);
    }

}
