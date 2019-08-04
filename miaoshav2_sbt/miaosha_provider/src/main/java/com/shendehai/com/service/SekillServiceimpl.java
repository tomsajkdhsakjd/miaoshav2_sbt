package com.shendehai.com.service;



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

    //设置资源池限流
    private static Semaphore resource = new Semaphore(500);

    @Override
    public List<Seckill> getSeckillList() {
        // TODO Auto-generated method stub
        //返回秒杀商品页
        return sd.queryAll();
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
    public Result excuteSeckill(Long seckillId,String phone) {
        // TODO Auto-generated method stub

        if (resource.getQueueLength() > 500) {

            return Result.error("秒杀商品已被抢购光了!");

        } else {
            try {
                resource.acquire();
                SeckillMessage seckillMessage = new SeckillMessage();
                seckillMessage.setSeckillId(seckillId.toString());
                seckillMessage.setPhone(phone);
                send.send(seckillMessage);
                return null;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                return Result.error("服务器繁忙-------");
            } finally {
                resource.release();
            }


        }

}


}
