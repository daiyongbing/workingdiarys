package com.iscas.workingdiarys.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeConsumer {
    @JmsListener(destination="authorization.queue")
    public void receiveQueue(String text){
        System.out.println("AuthorizeConsumer收到消息:"+text);
    }
}
