package com.iscas.workingdiarys.authorize;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:    用于授权解密的测试类,所有方式只是编码思路，不一定实现功能
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/29
 * @Version:        1.0
 */
public class AuthorizeManager {
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

    /**
     * @Description 申请授权
     * @author      daiyongbing
     * @param       requestParam 包含要解密的交易id，签名请求信息
     * @return
     * @date        2019/1/29
     */
    public void applAuthorization(JSONObject requestParam){
        // 将请求信息发送给授权人

    }

    /**
     * @Description 返回授权内容
     * @author      daiyongbing
     * @param       responseParam
     * @return
     * @date        2019/1/29
     */
    public JSONObject responseAuthorization(JSONObject responseParam){
        return responseParam;
    }

}
