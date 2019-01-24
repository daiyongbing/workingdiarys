package com.iscas.workingdiarys.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description:    用户证书实体类（和java.security.certificate不是一回事0）
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/24
 * @Version:        1.0
 */
public class Cert implements Serializable {
    private static final long serialVersionUID = -2921164128213228204L;
    private String userName;
    private String certNo;
    private String pemCert;
    private String certInfo;
    private String certAddr;
    private String certStatus;
    private String certLevel;
    private String privateKey;
    private String commonName;
    private Timestamp createTime;

    public Cert(){}

    public Cert(String userName, String certNo, String pemCert, String certInfo, String certAddr, String certStatus, String certLevel, String privateKey, String commonName, Timestamp createTime) {
        this.userName = userName;
        this.certNo = certNo;
        this.pemCert = pemCert;
        this.certInfo = certInfo;
        this.certAddr = certAddr;
        this.certStatus = certStatus;
        this.certLevel = certLevel;
        this.privateKey = privateKey;
        this.commonName = commonName;
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getPemCert() {
        return pemCert;
    }

    public void setPemCert(String pemCert) {
        this.pemCert = pemCert;
    }

    public String getCertInfo() {
        return certInfo;
    }

    public void setCertInfo(String certInfo) {
        this.certInfo = certInfo;
    }

    public String getCertAddr() {
        return certAddr;
    }

    public void setCertAddr(String certAddr) {
        this.certAddr = certAddr;
    }

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
    }

    public String getCertLevel() {
        return certLevel;
    }

    public void setCertLevel(String certLevel) {
        this.certLevel = certLevel;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
