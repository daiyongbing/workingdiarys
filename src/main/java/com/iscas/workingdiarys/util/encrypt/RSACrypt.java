package com.iscas.workingdiarys.util.encrypt;

import com.iscas.workingdiarys.util.encode.Base64Util;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
/**
 * @Description:    RSA数字签名算法
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/24
 * @Version:        1.0
 */
public class RSACrypt {
    public static final String ENCRYPTION_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM_MD5WITHRSA = "MD5withRSA";

    public static final String SIGNATURE_ALGORITHM_ECDSAWITHSHA256 = "ECDSAwithSHA256";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * @Description 生成数字签名
     * @author      daiyongbing
     * @param       encodePrivateKey 私钥（base64编码）
     * @param       data 待加密数据
     * @return      String 密文
     * @date        2019/1/24
     */
    public static String sign(byte[] data, String encodePrivateKey) throws Exception {
        byte[] keyBytes = Base64Util.decode2Bytes(encodePrivateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        // 取私钥匙对象
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_MD5WITHRSA);
        signature.initSign(privateKey);
        signature.update(data);
        return Base64Util.encode2String(signature.sign());
    }

    /**
     * @Description 校验数字签名
     * @author      daiyongbing
     * @param       encodepublicKey 公钥
     * @param       sign 数字签名
     * @param       data 加密数据
     * @return      true：成功 false：失败
     * @date        2019/1/24
     */
    public static boolean verify(byte[] data, String encodepublicKey, String sign) throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = Base64Util.decode2Bytes(encodepublicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        // 取公钥匙对象
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM_MD5WITHRSA);
        signature.initVerify(publicKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(Base64Util.decode2Bytes(sign));
    }

    /**
     * @Description 使用私钥解密（公钥加密）
     * @author      daiyongbing
     * @param       encodeData base64编码后的密文
     * @param       encodePrivateKey 私钥
     * @return      明文
     * @date        2019/1/24
     */
    public static byte[] decryptByPrivateKey(String encodeData, String encodePrivateKey) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Util.decode2Bytes(encodePrivateKey);
       // 取得加密数据
        byte[] data = Base64Util.decode2Bytes(encodeData);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * @Description 使用公钥解密（私钥加密）
     * @author      daiyongbing
     * @param       encodePublicKey 编码的公钥
     * @param       data 密文
     * @return      明文
     * @date        2019/1/24
     */
    public static byte[] decryptByPublicKey(byte[] data, String encodePublicKey) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Util.decode2Bytes(encodePublicKey);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * @Description 使用公钥加密
     * @author      daiyongbing
     * @param       data 待加密数据
     * @param       encodePublicKey 编码的公钥
     * @return      密文
     * @date        2019/1/24
     */
    public static byte[] encryptByPublicKey(byte[] data, String encodePublicKey) throws Exception {
        // 对公钥解密
        byte[] keyBytes = Base64Util.decode2Bytes(encodePublicKey);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * @Description 使用私钥加密数据
     * @author      daiyongbing
     * @param       data 待加密数据
     * @param       encodePrivateKey 私钥
     * @return      密文
     * @date        2019/1/24
     */
    public static byte[] encryptByPrivateKey(byte[] data, String encodePrivateKey) throws Exception {
        // 对密钥解码
        byte[] keyBytes = Base64Util.decode2Bytes(encodePrivateKey);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

/**
 * @Description 获取私钥
 * @author      daiyongbing
 * @param       keyMap
 * @return      私钥Base64编码字符串
 * @date        2019/1/24
 */
    public static String getPrivateKey(Map<String, Key> keyMap) throws Exception {
        PrivateKey key = (PrivateKey) keyMap.get(PRIVATE_KEY);
        return Base64Util.encode2String(key.getEncoded());
    }

    /**
     * @Description 获取公钥
     * @author      daiyongbing
     * @param       keyMap
     * @return      公钥Base64编码字符串
     * @date        2019/1/24
     */
    public static String getPublicKey(Map<String, Key> keyMap) throws Exception {
        PublicKey key = (PublicKey) keyMap.get(PUBLIC_KEY);
        return Base64Util.encode2String(key.getEncoded());
    }

    /**
     * @Description 初始化密钥对
     * @author      daiyongbing
     * @return      密钥对
     * @date        2019/1/24
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Map<String, Key> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Key> keyMap = initKey();
        String publicKey = getPublicKey(keyMap);
        String privateKey = getPrivateKey(keyMap);

        System.out.println("keyMap:"+keyMap);
        System.out.println("-----------------------------------");
        System.out.println("publicKey:"+publicKey);
        System.out.println("-----------------------------------");
        System.out.println("privateKey:"+privateKey);
        System.out.println("-----------------------------------");
        byte[] encryptByPrivateKey = encryptByPrivateKey("123456".getBytes(),privateKey);
        byte[] encryptByPublicKey = encryptByPublicKey("123456".getBytes(),publicKey);
        System.out.println("encryptByPrivateKey:"+new String(encryptByPrivateKey));
        System.out.println("-----------------------------------");
        System.out.println("encryptByPublicKey:"+new String(encryptByPublicKey));
        System.out.println("-----------------------------------");
        String sign = sign(encryptByPrivateKey,privateKey);
        System.out.println("sign:"+sign);
        System.out.println("-----------------------------------");
        boolean verify = verify(encryptByPrivateKey,publicKey,sign);
        System.out.println("verify:"+verify);
        System.out.println("-----------------------------------");
        byte[] decryptByPublicKey = decryptByPublicKey(encryptByPrivateKey,publicKey);
        byte[] decryptByPrivateKey = decryptByPrivateKey(Base64Util.encode2String(encryptByPublicKey),privateKey);
        System.out.println("decryptByPublicKey:"+new String(decryptByPublicKey));
        System.out.println("-----------------------------------");
        System.out.println("decryptByPrivateKey:"+new String(decryptByPrivateKey));

    }
}
