package com.shendehai.com.miaosha_provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@ComponentScan("com.shendehai")
@MapperScan("com.shendehai.com.mapper")
@EnableJms
public class MiaoshaProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoshaProviderApplication.class, args);
    }

}
