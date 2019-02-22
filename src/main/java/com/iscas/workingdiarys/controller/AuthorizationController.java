package com.iscas.workingdiarys.controller;

import com.alibaba.fastjson.JSONObject;
import com.iscas.workingdiarys.activemq.service.JmsProducerService;
import com.iscas.workingdiarys.service.UserService;
import com.iscas.workingdiarys.util.encode.Base64Util;
import com.iscas.workingdiarys.util.encrypt.MD5Utils;
import com.iscas.workingdiarys.util.encrypt.RSACrypt;
import com.iscas.workingdiarys.util.httpresponse.ResponseJson;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:    纯属测试，没什么卵用
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/29
 * @Version:        1.0
 */
@RestController
@RequestMapping("/authorize")
public class AuthorizationController {
    @Autowired
    private UserService userService;

    @Autowired
    private JmsProducerService jmsProducerService;

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static Map<String, Key> keyMap;

    static {
        try {
            keyMap = RSACrypt.initKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 模拟区块链数据上链
     * @author      daiyongbing
     * @param       cleartext 上链密文
     * @return      tid 交易id
     * @date        2019/1/30
     */
    @RequestMapping("putdata")
    @Transactional(rollbackFor = Exception.class)
    public String putProof(@RequestParam String  cleartext, @RequestParam String userName){
        byte[] cipherBytes;
        try {
            cipherBytes = RSACrypt.encryptByPrivateKey(cleartext.getBytes(), RSACrypt.getPrivateKey(keyMap));   //使用私钥加密明文
            userService.putProof(MD5Utils.stringMD5(cleartext), userName, Base64Util.encode2String(cipherBytes), RSACrypt.getPrivateKey(keyMap), RSACrypt.getPublicKey(keyMap));
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return MD5Utils.stringMD5(cleartext);
    }

    /**
     * @Description 模拟获取上链数据
     * @author      daiyongbing
     * @param       userName 请求者名称，实际中需要验证签名，这里为了测试方便，只要数据库中有该用户就认为是合法的
     * @return      密文
     * @date        2019/1/30
     */
    @RequestMapping("getdata")
    public void getData(HttpServletResponse response, HttpServletRequest request, @RequestParam String userName, @RequestParam String txid){
        String cipherText = "";
        if (userService.existUser(userName)){
            cipherText = userService.getData(txid);
        }
        ResponseJson.jsonResult(response, request, 200, cipherText);
    }

    /**
     * @Description 申请授权
     * @author      daiyongbing
     * @param       requestParam 包含要解密的交易id，签名请求信息，这里不做真正的签名验证，只是模拟一个过程
     * @return      申请结果
     * @date        2019/1/29
     */
    @RequestMapping("applyauth")
    public String applyAuthorization(@RequestBody JSONObject requestParam){
        // 将请求信息发送给授权人
        Destination destination = new ActiveMQQueue("applyauth.queue");
        jmsProducerService.sendMessage(destination, requestParam.toJSONString());
        return "申请已发送";
    }

    /**
     * @Description 返回授权内容
     * @author      daiyongbing
     * @param       responseParam
     * @return
     * @date        2019/1/29
     */
    public JSONObject responseAuthorization(JSONObject responseParam){
        //
        Destination destination = new ActiveMQQueue("authorization.queue");
        jmsProducerService.sendMessage(destination, responseParam.toJSONString());
        return responseParam;
    }

    /**
     * @Description 获取公钥
     * @author      daiyongbing
     * @param
     * @return
     * @date        2019/1/29
     */
    public void applyPublicKey(HttpServletRequest request, HttpServletResponse response, @RequestParam String token){
        Map<String, String> keyMap = new HashMap<>();
        keyMap.get(token);
    }

}
