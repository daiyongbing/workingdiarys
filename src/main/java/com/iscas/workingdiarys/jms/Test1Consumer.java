package com.iscas.workingdiarys.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Test1Consumer {
    @JmsListener(destination="test1.queue")
    public void receiveQueue(String text){
        System.out.println("Test1Consumer收到消息:"+text);
    }
}
