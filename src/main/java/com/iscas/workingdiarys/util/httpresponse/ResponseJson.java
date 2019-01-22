package com.iscas.workingdiarys.util.httpresponse;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:    返回给客户端的json数据封装
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/21
 * @Version:        1.0
 */
public class ResponseJson {

    public static void jsonResult(HttpServletResponse response, HttpServletRequest request, int status, Object result){
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(status);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
