package com.shendehai.com.service;


import com.shendehai.com.common.entity.Seckill;
import com.shendehai.com.mapper.SeckillDao;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.concurrent.*;

@Service
public class ICreateHtmlServiceimpl implements  ICreateHtmlService{
    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //多线程生成静态页面
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1000));
    @Autowired
    private Configuration configuration;

    @Autowired
    private SeckillDao seckillDao;

    @Value("F:\\IDEA_Project\\miaosha_sbt\\src\\main\\resources\\templates\\")
    private String path;
    @Override
    public String CreateItemDeatilHtml(long id) {
         Seckill querybyid = seckillDao.querybyid(id);
        createhtml createhtml = new createhtml(querybyid);
        //异步化
        FutureTask<String> stringFutureTask = new FutureTask<>(createhtml);
        executor.submit(stringFutureTask);
        try {
            String s = stringFutureTask.get();
            if(s.equals("success")){
                return id+"";
            }
        } catch (InterruptedException e) {
            System.out.println("静态页面生成异常！");
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return id+"";
    }
    class createhtml implements Callable<String> {
        Seckill seckill;

        public createhtml(Seckill seckill) {
            this.seckill = seckill;
        }
        @Override
        public String call() throws Exception {
            Template template = configuration.getTemplate("goods.flt");
            File file= new File(path+seckill.getSeckillId()+".html");
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            template.process(seckill, writer);
            return "success";
        }
    }
}

