package com.iscas.workingdiarys.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiarys.entity.Cert;
import com.iscas.workingdiarys.entity.CertInfo;
import com.iscas.workingdiarys.entity.ResponseStatus;
import com.iscas.workingdiarys.entity.User;
import com.iscas.workingdiarys.activemq.service.JmsProducerService;
import com.iscas.workingdiarys.config.SystemProperties;
import com.iscas.workingdiarys.service.UserService;
import com.iscas.workingdiarys.util.cert.CertUtil;
import com.iscas.workingdiarys.util.httpresponse.ResponseData;
import com.iscas.workingdiarys.util.httpresponse.ResponseJson;
import com.iscas.workingdiarys.util.jjwt.JWTTokenUtil;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 * @Description:    UserController
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/18
 * @Version:        1.0
 */
@RestController
@RequestMapping("/user")

public class UserController {
    Logger logger = LoggerFactory.getLogger(getClass());

   @Autowired
   private UserService userService;

   @Autowired
   private SystemProperties propertiesService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


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
            checkUser = userService.selectOneByName(userName);  //验证用户名
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

    /**
     * @Description 验证userid是否已被注册
     * @author      daiyongbing
     * @param       userId
     * @return
     * @date        2019/1/22
     */
    @GetMapping(value = "checkid", produces = MediaType.APPLICATION_JSON_VALUE)
    public void checkUserId(HttpServletResponse response, HttpServletRequest request, @RequestParam String userId){
        if (userId == null || "".equals(userId)){
            ResponseJson.jsonResult(response, request, ResponseStatus.CLIENT_INVALIED_PARAM, new ResponseData(ResponseStatus.CLIENT_INVALIED_PARAM,"参数不能为空"));
            return;
        }
        User checkUser = null;
        try {
            checkUser = userService.selectOneById(userId);  //验证用户ID
            if (checkUser == null){
                ResponseJson.jsonResult(response, request, ResponseStatus.SUCCESS, new ResponseData(200,"ID可用"));
            } else {
                ResponseJson.jsonResult(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR, new ResponseData(ResponseStatus.DB_ALREADY_EXIST_ERROR,"ID已存在"));
            }
        } catch (Exception e){
            e.printStackTrace();
            ResponseJson.jsonResult(response, request, ResponseStatus.SERVER_ERROR, new ResponseData(ResponseStatus.SERVER_ERROR,"服务器异常"));
        }
    }

    /**
     * @Description 注册接口，当发生异常时必须做数据库事物回滚，保证数据的有效性
     * @author      daiyongbing
     * @param       usercertJson 包含用户注册信息以及证书生成信息
     * @date        2019/1/24
     */
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(rollbackFor = Exception.class)
    public void register(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject usercertJson){
        Cert cert = null;
        User user;
        JSONObject certJson = usercertJson.getJSONObject("certInfo");
        try{
            user = usercertJson.getJSONObject("userInfo").toJavaObject(User.class);
            if (certJson != null && certJson.size()>0){
                cert = CertUtil.genCert(certJson.toJavaObject(CertInfo.class), propertiesService.getJksPath(), propertiesService.getCertPath());
                cert.setUserName(user.getUserName());
                user.setCertNo(cert.getCertNo());
            }
            user.setRegisterTime(new Timestamp(System.currentTimeMillis()));
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.register(user, cert);
            ResponseJson.jsonResult(response, request, ResponseStatus.SUCCESS, new ResponseData(200, "注册成功"));
        } catch (DuplicateKeyException de){
            System.err.println(de.getCause());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ResponseJson.jsonResult(response, request, ResponseStatus.DB_ALREADY_EXIST_ERROR, new ResponseData(309, "用户已存在"));
            return;
        } catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ResponseJson.jsonResult(response, request, ResponseStatus.SERVER_ERROR, new ResponseData(500, "服务器异常"));
        }
    }

    /**
     * @Description 用户更新资料（注册时间、用户名、用户ID、密码不允许更新）
     * @author      daiyongbing
     * @param       user
     * @param       response
     * @param       request
     * @date        2019/1/25
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateUserByName(HttpServletResponse response, HttpServletRequest request, @RequestBody User user){
        String token = request.getHeader("Authorization");
        String userName;
        try{
            userName = JWTTokenUtil.parseToken(token).getSubject();
            user.setUserName(userName);
            userService.updateUser(user);
            ResponseJson.jsonResult(response, request, ResponseStatus.SUCCESS, "信息已更新");
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //事物回滚
            System.err.println(e.getCause().getMessage());
            ResponseJson.jsonResult(response, request, ResponseStatus.SERVER_ERROR, "服务器异常");
        }
    }

    /**
     * @Description 修改密码，验证原密码修改，以后可能会验证手机短信
     * @author      daiyongbing
     * @param       jsonObject
     * @date        2019/1/25
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "password/change", produces = MediaType.APPLICATION_JSON_VALUE)
    public void modifyPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject){
        String oldPassword = jsonObject.getString("oldPassword");
        String newPassword = jsonObject.getString("newPassword");
        String authHead = request.getHeader("Authorization");
        String userName = JWTTokenUtil.parseToken(authHead).getSubject();
        try {
            User user = userService.selectOneByName(userName);
            if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
                userService.changePassword(userName, newPassword);
                ResponseJson.jsonResult(response, request, ResponseStatus.SUCCESS, "success");
            } else {
                ResponseJson.jsonResult(response, request, ResponseStatus.CLIENT_INVALIED_PASSWORD, "密码验证错误");
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); //事物回滚
            System.err.println(e.getCause().getMessage());
            ResponseJson.jsonResult(response, request, ResponseStatus.SERVER_ERROR, "服务器异常");
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
