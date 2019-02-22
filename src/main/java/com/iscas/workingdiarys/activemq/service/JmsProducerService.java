package com.iscas.workingdiarys.activemq.service;

import javax.jms.Destination;

public interface JmsProducerService {
    /**
     *
     * @author      daiyongbing
     * @date        2019/1/21
     */
    public void sendMessage(Destination destination, final String message);

    /**
     * 功能描述：使用默认消息队列， 发送消息
     * @param message
     */
    public void sendMessage( final String message);


    /**
     * 功能描述：消息发布者
     * @param msg
     */
    public void publish(String msg);

}
