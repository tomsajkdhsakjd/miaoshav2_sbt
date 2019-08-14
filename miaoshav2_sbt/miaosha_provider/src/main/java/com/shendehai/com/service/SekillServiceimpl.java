package com.shendehai.com.service;


import com.shendehai.com.Redis.RedisUtil;
import com.shendehai.com.activemq.ActivemqSend;
import com.shendehai.com.common.entity.Result;
import com.shendehai.com.common.entity.Seckill;
import com.shendehai.com.enums.SeckillStatEnum;
import com.shendehai.com.mapper.SeckillDao;
import entity.SeckillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.concurrent.Semaphore;

@Service
public class SekillServiceimpl implements SeckillService {
    @Autowired
    private SeckillDao sd;
    @Autowired
    private ActivemqSend send;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public List<Seckill> getSeckillList() {
        // TODO Auto-generated method stub
        //返回秒杀商品页
        List<Seckill> seckills = sd.queryAll();
        for (Seckill s : seckills
        ) {
            redisUtil.set(s.getSeckillId() + "", s.getNumber());
        }
        return seckills;
    }

    @Override
    public Seckill getSeckillByid(long id) {
        // TODO Auto-generated method stub
        return sd.querybyid(id);
    }

    @Override
    public Result startSeckil(long killId, long userId) {


        Integer i = sd.delSeckillone(killId);
        return Result.ok(SeckillStatEnum.SUCCESS);

    }

    @Override
    public String MD5Url(long seckillId) {
        // TODO Auto-generated method stub

        String base = seckillId + "/" + "salt";
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;

    }

    @Override
    public void excuteSeckill(Long seckillId, String token_id) {
        // TODO Auto-generated method stub
        String phone = (String) redisUtil.get(token_id);
        try {
            //通过redis达到预减的作用
            long decr = redisUtil.decr(seckillId.toString(), 1l);
            if (decr < 0) {
                redisUtil.setnx(phone, "秒杀商品已被抢购光了!");
            } else {
                SeckillMessage seckillMessage = new SeckillMessage();
                seckillMessage.setSeckillId(seckillId.toString());
                seckillMessage.setPhone(phone);
                send.send(seckillMessage);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            redisUtil.setnx(phone, "服务器繁忙!");
        }


    }

}



