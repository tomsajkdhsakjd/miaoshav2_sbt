package com.shendehai.com.activemq;

import entity.SeckillMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

public class SeckillMessageConvert implements MessageConverter {
    public Message toMessage(Object object, Session session)
            throws JMSException, MessageConversionException {

        System.out.println("sendMessage:"+object.toString());
        ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
        msg.setObject((Serializable) object);
        return msg;
    }

    public Object fromMessage(Message message) throws JMSException,
            MessageConversionException {

        SeckillMessage bus = null;
        if(message instanceof ActiveMQObjectMessage){
            ActiveMQObjectMessage aMsg = (ActiveMQObjectMessage) message;
            bus=(SeckillMessage) aMsg.getObject();
        }
        return bus;
    }

}
