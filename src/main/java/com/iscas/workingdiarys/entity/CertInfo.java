package com.iscas.workingdiarys.entity;

import java.io.Serializable;

/**
 * @Description:    用于生成Certificate的信息
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/24
 * @Version:        1.0
 */
public class CertInfo implements Serializable {
    private static final long serialVersionUID = 4997550424285351968L;
    private String CN;  //Common Name
    private String OU;  //Organization Unit
    private String O;   //Organization Name
    private String C;   //County
    private String L;   //Locality Name
    private String ST; //State Name
    private String cPassword;

    public String getCN() {
        return CN;
    }

    public void setCN(String CN) {
        this.CN = CN;
    }

    public String getOU() {
        return OU;
    }

    public void setOU(String OU) {
        this.OU = OU;
    }

    public String getO() {
        return O;
    }

    public void setO(String o) {
        O = o;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }
}
