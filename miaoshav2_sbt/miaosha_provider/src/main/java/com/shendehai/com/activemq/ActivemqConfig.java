package com.shendehai.com.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;


@Configuration
public class ActivemqConfig {
    //定义点到点队列
    @Bean
    public Queue queue(){
        return new ActiveMQQueue("queue");
    }
}
