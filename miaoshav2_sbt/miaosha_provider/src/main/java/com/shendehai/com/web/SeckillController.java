package com.shendehai.com.web;

import com.shendehai.com.Redis.RedisUtil;
import com.shendehai.com.common.entity.Result;
import com.shendehai.com.common.entity.Seckill;
import com.shendehai.com.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/seckill")
public class SeckillController {
    private static final String SeckillController = null;
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //创建线程池  调整队列数 拒绝服务
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize + 1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));


    @Autowired
    private SeckillService ss;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String Toindex() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/seckillPage/list")
    public Result getSeckillList() {
        System.out.println(999);
        List<Seckill> list = ss.getSeckillList();
        Result ok = Result.ok(list);
        System.out.println(ok);
        return ok;

    }

    @ResponseBody
    @RequestMapping(value = "/getnowtime")
    public Result getnowtime() {
        Date date = new Date();
        return Result.ok(date);

    }

    @ResponseBody
    @RequestMapping(value = "/{seckillId}/getkillurl")
    public Result getkillUrl(@PathVariable("seckillId") long seckillId) {

        String url = ss.MD5Url(seckillId);

        return Result.ok(url);
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution")
    @ResponseBody
    public void excutekill(@PathVariable("seckillId") Long seckillId, @CookieValue("userPhone") String phone) {
      ss.excuteSeckill(seckillId,phone);
    }

    @RequestMapping(value ="/user/state")
    @ResponseBody
    public Result getkilluserstate(String phone){
        Object o = redisUtil.get(phone);
        if(o!=null){
            System.out.println(o.toString());
            return Result.ok(o);
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/startkill")
    public Result start(long seckillId) {
        int skillNum = 1000;
        final CountDownLatch latch = new CountDownLatch(skillNum);//N个购买者
        final long killId = seckillId;
        for (int i = 0; i < skillNum; i++) {
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    Result result = ss.startSeckil(killId, userId);
                    if (result != null) {
                        System.out.printf("用户:{}{}", userId, result.get("msg"));
                    } else {
                        System.out.printf("用户:{}{}", userId, "哎呦喂，人也太多了，请稍后！");
                    }

                }
            };
            executor.execute(task);

        }

        return Result.ok();


    }

}
  