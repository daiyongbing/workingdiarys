package com.iscas.workingdiarys.util.cert;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiarys.entity.Cert;
import com.iscas.workingdiarys.entity.CertInfo;
import com.iscas.workingdiarys.util.certificate.CertificateUtil;
import com.iscas.workingdiarys.util.encode.Base64Util;
import com.iscas.workingdiarys.util.encrypt.MD5Utils;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;

/**
 * @Description:    Cert工具类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/24
 * @Version:        1.0
 */
public class CertUtil {
    /**
     * @Description 构造Cert对象
     * @author      daiyongbing
     * @param       certinfo password用于私钥加密和解密，不做保存，一旦忘记不可找回
     * @param       jks_path
     * @param       certPath
     * @return      Cert
     * @date        2019/1/24
     */
    public static Cert genCert(CertInfo certinfo, String jks_path, String certPath){
        String[] certInfo= {certinfo.getCN(), certinfo.getOU(), certinfo.getO(),
                certinfo.getC(), certinfo.getL(), certinfo.getST()};
        String password = certinfo.getcPassword();

        KeyPair keyPair = null;
        X509Certificate certificate = null;
        String pemCert = "";
        String certNo = "";
        String encyptPrivateKey = "";
        try {
            keyPair = CertificateUtil.generateKeyPair();
            certificate = CertificateUtil.generateCert(certInfo, keyPair);    // 生成用户证书
            pemCert = Base64Util.encode2String(CertificateUtil.getPemCertString(certificate));     // 获取pemcert,并做Base64编码
            certNo = MD5Utils.stringMD5(pemCert); //使用pemCert的MD5作为证书编号
            encyptPrivateKey = CertificateUtil.encryptPrivateKey(keyPair.getPrivate(), password); //加密私钥
        }catch (Exception e){
            e.getMessage();
        }

        Cert cert = new Cert(certinfo.getCN(), certNo, pemCert, Base64Util.encode2String(JSON.toJSONString(certinfo)), null,
                "0", "0", encyptPrivateKey, certinfo.getCN(), new Timestamp(System.currentTimeMillis()));
        try {
            CertificateUtil.generateJksWithCert(certificate, keyPair, password, jks_path, certInfo[0]);   //保存jks文件到服务器
            CertificateUtil.savePemCertAsFile(certificate, certPath, certInfo[0]); // 保存cer到服务器
        }catch (Exception e){
            e.printStackTrace();
        }
        return cert;
    }
}
