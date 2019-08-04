package com.shendehaizi.activemq;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

@Configuration
public class JMSConfig {

    @Value("${spring.activemq.user}")
    private String usrName;

    @Value("${spring.activemq.password}")
    private  String password;

    @Value("${spring.activemq.broker-url}")
    private  String brokerUrl;
    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(usrName, password, brokerUrl);
    }
    @Bean
    public JmsListenerContainerFactory queueContainer(ActiveMQConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
       connectionFactory.setTrustAllPackages(true);
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(Boolean.FALSE);//支持queue

        return  factory;
    }

}
