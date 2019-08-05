package com.shendehaizi.activemq;

import com.shendehaizi.Order.OrderSerivce;
import entity.SeckillMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;


@Service
public class ActivemqConsume {


    @Autowired
    private OrderSerivce orderSerivce;

    // 使用JmsListener配置消费者监听的队列，其中name是接收到的消息
    @JmsListener(destination = "queue", containerFactory = "queueContainer")
    public void handleMessage(Message m, Session session) throws JMSException {
        SeckillMessage seckillMessage = new SeckillMessage();
        if (m instanceof ObjectMessage) {
            ObjectMessage objectMessage = (ObjectMessage) m;
            try {
                seckillMessage = (SeckillMessage) objectMessage.getObject();
                System.out.println(seckillMessage);
                orderSerivce.excultdelseckill(seckillMessage);
                int i=1/0;
                m.acknowledge();
            } catch (Exception e) {
                session.recover();// 此不可省略 重发信息使用
                e.printStackTrace();
            }
        }
    }


}
