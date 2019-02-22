package com.iscas.workingdiarys.activemq.service;

import com.iscas.workingdiarys.activemq.service.JmsProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.Topic;

@Service
public class JmsProducerServiceImp implements JmsProducerService {
    @Autowired
    private Queue queue;

    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Override
    public void sendMessage(Destination destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    @Override
    public void sendMessage(String message) {
        jmsTemplate.convertAndSend(message);
    }

//=======发布订阅相关代码=========

    @Autowired
    private Topic topic;
    @Override
    public void publish(String msg) {
        jmsTemplate.convertAndSend(this.topic, msg);
    }

}
