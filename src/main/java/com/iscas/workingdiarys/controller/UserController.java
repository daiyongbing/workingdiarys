package com.iscas.workingdiarys.controller;

import com.iscas.workingdiarys.entity.ResponseStatus;
import com.iscas.workingdiarys.entity.User;
import com.iscas.workingdiarys.service.JmsProducerService;
import com.iscas.workingdiarys.service.UserService;
import com.iscas.workingdiarys.util.httpresponse.ResponseData;
import com.iscas.workingdiarys.util.httpresponse.ResponseJson;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:    UserController
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/18
 * @Version:        1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

   @Autowired
   private UserService userService;


    /**
     * @Description 检测username是否已被注册
     * @author      daiyongbing
     * @param       userName
     * @param       null
     * @return
     * @date        2019/1/22
     */
    @GetMapping(value = "checkname", produces = MediaType.APPLICATION_JSON_VALUE)
    public void checkUserName(HttpServletResponse response, HttpServletRequest request, @RequestParam String userName){
        if (userName == null || "".equals(userName)){
            ResponseJson.jsonResult(response, request, ResponseStatus.CLIENT_INVALIED_PARAM, new ResponseData(ResponseStatus.CLIENT_INVALIED_PARAM,"参数不能为空"));
            return;
        }
        User checkUser = null;
        try {
            checkUser = userService.selectOne(userName);  //验证用户名
            if (checkUser == null){
                ResponseJson.jsonResult(response, request, ResponseStatus.SUCCESS, new ResponseData(200,"用户名可用"));
            } else {
                ResponseJson.jsonResult(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR, new ResponseData(ResponseStatus.DB_ALREADY_EXIST_ERROR,"用户已存在"));
            }
        } catch (Exception e){
            e.printStackTrace();
            ResponseJson.jsonResult(response, request, ResponseStatus.SERVER_ERROR, new ResponseData(ResponseStatus.SERVER_ERROR,"服务器异常"));
        }
    }


    @Autowired
    private JmsProducerService producerService;
    /**
     * 功能描述：微信支付回调接口
     * @param msg 支付信息
     * @return
     */
    @GetMapping("test2")
    public Object test2(String msg){

        Destination destination = new ActiveMQQueue("test2.queue");

        producerService.sendMessage(destination, msg);

        return "Success";
    }



    @GetMapping("test1")
    public Object test1(String msg){
        producerService.sendMessage(msg);
        return "Success";
    }
}
