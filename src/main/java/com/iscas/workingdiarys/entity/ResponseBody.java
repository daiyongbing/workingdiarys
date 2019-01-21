package com.iscas.workingdiarys.entity;

import java.io.Serializable;

/**
 * @Description:    http响应实体类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/21
 * @Version:        1.0
 */
public class ResponseBody implements Serializable {
    private static final long serialVersionUID = -6622357186410076382L;
    private int code;
    private String message;
    private Object result;
    private String jwtToken;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
