package com.shendehaizi.Order;

import com.shendehaizi.Redis.RedisUtil;
import com.shendehaizi.entity.OrderInfo;
import com.shendehaizi.entity.Seckill;
import com.shendehaizi.locks.Locker;
import com.shendehaizi.mapper.SeckillDao;
import com.shendehaizi.mapper.SeckillOrder;
import com.shendehaizi.mapper.SucessSeckillDao;
import entity.SeckillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderSerivce {
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SucessSeckillDao sucessSeckillDao;
    @Autowired
    private SeckillOrder seckillOrder;

    @Autowired
    private Locker locker;

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
                Seckill querybyid = seckillDao.querybyid(Long.valueOf(seckillId));
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setSeckild_id(Integer.valueOf(seckillId));
                orderInfo.setSeckill_name(querybyid.getName());
                orderInfo.setUsr_phone(phone);
                seckillOrder.insertOrderdetail(orderInfo);

            }
        }
    }

    //每隔3分钟执行一次查询秒杀成功但是在五分钟之内没有完成下单逻辑的用户数据进行回仓
    @Scheduled(cron ="0 */1 * * * ?")
    public void datawareHousing(){
        List<OrderInfo> getnotpayorder = seckillOrder.getnotpayorder();
        if(getnotpayorder!=null){
            //申请分布式锁
            System.out.println("开始了");
          locker.lock("lock_key",1000);
            try {
                for (OrderInfo o: getnotpayorder
                     ) {
                    System.out.println(o.getOrder_id());
                    System.out.println(o.toString());
                    seckillOrder.delnotpayorder(o.getOrder_id());
                    sucessSeckillDao.delnotpayorder(o.getSeckild_id());
                    seckillDao.addseckillnum(o.getSeckild_id());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                locker.unlock("lock_key");
            }

        }


    }




}
