package com.iscas.workingdiarys.util.encrypt;

import com.alibaba.fastjson.JSON;
import com.iscas.workingdiarys.entity.Key;
import com.iscas.workingdiarys.util.certificate.CertificateUtil;

import java.io.File;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.List;

/**
 * ECDSAwithSha256签名算法
 */
public class ECDSAwithSha256 {

    /**
     * 数字签名（最终签名经过base64编码成字符串）
     * @param privateKey ecc私钥
     * @param content 明文
     * @return
     */
    public static String sign(PrivateKey privateKey, String content){
        String sig = null;
        try {
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initSign(privateKey);
            signature.update(content.getBytes());
            sig = Base64.getEncoder().encodeToString(signature.sign());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return sig;
    }

    /**
     * 数字签名认证
     * @param content 明文
     * @param publicKey 公钥
     * @param sign 原始签名
     * @return
     */
    public static boolean verify(String content, PublicKey publicKey, byte[] sign){
        Signature signature = null;
        boolean flag = false;
        try {
            signature = Signature.getInstance("SHA1withECDSA");
            signature.initVerify(publicKey);
            signature.update(content.getBytes());
            flag = signature.verify(sign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) throws Exception{
        String value = "90后码农";
        /*KeyPair keyPair = CertificateUtil.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();*/
        List list = CertificateUtil.getCertFromJKS(new File("jks/mykeystore_2.jks"), "123", "2");
        PrivateKey privateKey = (PrivateKey) list.get(1);
        Certificate certificate = (Certificate) list.get(0);
        PublicKey publicKey = certificate.getPublicKey();
        Key key = new Key("daiyongbing","");
        String sign = sign(privateKey, JSON.toJSONString(key));
        System.out.println("签名："+sign);
        key.setSign(sign);
        String signKey = JSON.toJSONString(key);
        System.out.println("签名后的key:"+signKey);
        System.out.println("加密后的key:"+Base64.getEncoder().encodeToString(CustomeCrypt.encryptByPrivateKey(signKey.getBytes(), privateKey)));
        System.out.println("加密后的value："+Base64.getEncoder().encodeToString(CustomeCrypt.encryptByPrivateKey(value.getBytes(), privateKey)));
        /*******************签名验证*****************/
        String oldSign = key.getSign();
        key.setSign("");
        System.out.println("签名验证结果："+verify(JSON.toJSONString(key), publicKey, Base64.getDecoder().decode(oldSign)));
    }
}
