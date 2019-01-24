package com.iscas.workingdiarys.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Description:    获取application.properties属性值
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/22
 * @Version:        1.0
 */
@Service
public class PropertiesService {
    @Value("${cer.path}")
    private String certPath;

    @Value("${jks.path}")
    private String jksPath;


    public String getCertPath() {
        return certPath;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    public String getJksPath() {
        return jksPath;
    }

    public void setJksPath(String jksPath) {
        this.jksPath = jksPath;
    }
}
