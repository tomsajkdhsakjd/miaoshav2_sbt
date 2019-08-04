package com.shendehai.com.activemq;

import entity.SeckillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;


@Service
public class ActivemqSend {
    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsMessagingTemplate;

    public void send(SeckillMessage name) {
        jmsMessagingTemplate.setMessageConverter(new SeckillMessageConvert());
        jmsMessagingTemplate.convertAndSend(queue,name);
        }
    }

