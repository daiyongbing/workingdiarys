package com.iscas.workingdiarys.activemq.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Test2Consumer {
    @JmsListener(destination="test2.queue")
    public void receiveQueue(String text){
        System.out.println("Test2Consumer收到消息:"+text);
    }
}
