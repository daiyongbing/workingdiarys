package com.iscas.workingdiarys.entity;

public class Key {
    private String key;
    private String sign;

    public Key(String key, String sign) {
        this.key = key;
        this.sign = sign;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
