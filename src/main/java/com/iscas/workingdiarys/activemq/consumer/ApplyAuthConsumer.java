package com.iscas.workingdiarys.activemq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiarys.activemq.service.JmsProducerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

@Component
public class ApplyAuthConsumer {
    @Autowired
    JmsProducerService producerService;

    @JmsListener(destination="applyauth.queue")
    public void receiveQueue(String text){
        System.out.println("ApplyAuthConsumer收到消息:"+text);
        JSONObject object = JSON.parseObject(text);
        if (object.getString("name") != null){
            Destination destination = new ActiveMQQueue("authorization.queue");
            producerService.sendMessage(destination, "同意授权");
        }
    }
}
