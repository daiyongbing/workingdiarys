package com.iscas.workingdiarys.jms;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicSubject {
    @JmsListener(destination="sign.topic", containerFactory="jmsListenerContainerTopic")
    public void receive1(String text){
        System.out.println("sign.topic 消费者:receive1="+text);
    }


    @JmsListener(destination="sign.topic", containerFactory="jmsListenerContainerTopic")
    public void receive2(String text){
        System.out.println("sign.topic 消费者:receive2="+text);
    }


    @JmsListener(destination="sign.topic", containerFactory="jmsListenerContainerTopic")
    public void receive3(String text){
        System.out.println("sign.topic 消费者:receive3="+text);
    }
}
