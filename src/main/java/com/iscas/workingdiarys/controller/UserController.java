package com.iscas.workingdiarys.controller;

import com.iscas.workingdiarys.entity.User;
import com.iscas.workingdiarys.service.JmsProducerService;
import com.iscas.workingdiarys.service.UserService;
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

   /**
    *
    * @author      daiyongbing
    * @param
    * @return
    * @exception
    * @date        2019/1/18
    */
   @Autowired
   private UserService userService;



    @GetMapping(value = "checkname", produces = MediaType.APPLICATION_JSON_VALUE)
    public User checkUserName(HttpServletResponse response, HttpServletRequest request, @RequestParam String userName){
        User checkUser = null;
        try {
            checkUser = userService.selectOne(userName);  //验证用户名
            /*if (checkUser == null){
                JsonResult.resultJson(response, request, ResponseStatus.SUCCESS, new ResultData("用户名可用"));
                return checkUser;
            } else {
                JsonResult.resultJson(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR,  new ResultData("用户已存在"));
            }*/
        } catch (Exception e){
            e.printStackTrace();
            //JsonResult.resultJson(response, request, ResponseStatus.SERVER_ERROR,  new ResultData("服务器异常"));
        }
        return checkUser;
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
