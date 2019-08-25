package com.shendehai.com.web;

import com.shendehai.com.Redis.RedisUtil;
import com.shendehai.com.annotion.AccessLimit;
import com.shendehai.com.common.entity.Result;
import com.shendehai.com.common.entity.Seckill;
import com.shendehai.com.locks.Locker;
import com.shendehai.com.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    @Autowired
    private Locker locker;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String Toindex() {
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/seckillPage/list")
    public Result getSeckillList() {
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
    @AccessLimit(maxCount = 1)
    public void excutekill(@PathVariable("seckillId") Long seckillId, @CookieValue("token_id") String phone) {
        while(locker.isLocked("lock_key")) {
            System.out.println("上面有锁");

        }
            ss.excuteSeckill(seckillId, phone);

    }

    @RequestMapping(value = "/user/state")
    @ResponseBody
    public Result getkilluserstate(String phone) {
        Object o = redisUtil.get(phone);
        Object o1 = redisUtil.get((String) o);
        if (o != null) {
            return Result.ok(o1);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/user/login")
    @ResponseBody
    public Result login(String phone, HttpServletResponse response) {
        String s = UUID.randomUUID().toString();
        boolean setnx = redisUtil.setnx(s, phone, 6000);
        Cookie token_id = new Cookie("token_id", s);
        token_id.setPath("/");
        token_id.setMaxAge(6000);
        response.addCookie(token_id);
        return Result.ok(s);
    }

    @RequestMapping(value = "/user/getloginstatus")
    @ResponseBody
    public Result getloginstatus(String tokenid) {
        Object o = redisUtil.get(tokenid);
        return Result.ok(o);
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
  