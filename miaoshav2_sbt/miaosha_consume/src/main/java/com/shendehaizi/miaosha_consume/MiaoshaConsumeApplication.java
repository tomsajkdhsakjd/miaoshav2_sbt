package com.shendehaizi.miaosha_consume;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@ComponentScan("com.shendehaizi")
@MapperScan("com.shendehaizi.mapper")
@EnableScheduling
public class MiaoshaConsumeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaConsumeApplication.class, args);
    }

}
