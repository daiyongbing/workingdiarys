package com.iscas.workingdiarys.util.httpresponse;

import com.iscas.workingdiarys.entity.ResponseStatus;
import java.util.List;
import java.util.Map;

/**
 * @Description:    http响应数据构造
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/21
 * @Version:        1.0
 */
public class ResponseData {
    private int code;
    private String message;
    private Object datas;

    public ResponseData(int code) {this.code = code; }

    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseData(int code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.datas = obj;
    }


    /**
     * 返回单个实体
     */
    public <T> ResponseData(T entity) {
        this.code = 200;
        this.message = "success";
        this.datas = entity;
    }

    /**
     * 返回集合类型
     */
    public ResponseData(List<?> list) {
        this.code = 200;
        this.message = "success";
        this.datas = list;
    }

    /**
     * 返回Map集合类型
     */
    public ResponseData(Map<String, Object> map) {
        this.code = 200;
        this.message = "success";
        this.datas = map;
    }


    /**
     * 运行时异常
     */
    public ResponseData(RuntimeException rex) {
        this.code = ResponseStatus.SERVER_OTHER_UNKNOWN_ERROR;
        this.message = rex.getMessage();
    }

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

    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }
}
