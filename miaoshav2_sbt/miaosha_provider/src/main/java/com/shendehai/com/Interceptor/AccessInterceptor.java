package com.shendehai.com.Interceptor;

import com.shendehai.com.Redis.RedisUtil;
import com.shendehai.com.annotion.AccessLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Semaphore;

@Configuration
public class AccessInterceptor extends HandlerInterceptorAdapter {
    //设置资源池限流ce
    private static Semaphore resource = new Semaphore(500);
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            AccessLimit methodAnnotation = method.getMethodAnnotation(AccessLimit.class);
            if (methodAnnotation == null) {
                return true;
            }
            if (resource.getQueueLength() > 100) {
                String phone = (String) redisUtil.get(request.getCookies()[0].getValue());
                if (phone != null) {
                    redisUtil.setnx(phone, "秒杀商品已被抢购光了!");
                }
                return false;
            } else {
                resource.acquire();
                return true;
            }

        } else {
            return true;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {

        } finally {
            resource.release();
        }
    }
}
