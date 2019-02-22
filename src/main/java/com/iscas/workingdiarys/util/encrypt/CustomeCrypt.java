package com.iscas.workingdiarys.util.encrypt;

import com.iscas.workingdiarys.util.certificate.CertificateUtil;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.security.*;
import java.security.interfaces.ECPrivateKey;

public class CustomeCrypt {
    /**
     * @Description 使用私钥加密数据
     * @author      daiyongbing
     * @param       data 待加密数据
     * @param       privateKey 私钥
     * @return      密文
     * @date        2019/1/24
     */
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        ECPrivateKey ecPrivateKey = (ECPrivateKey)privateKey;
        byte[] keyBytes = privateKey.getEncoded();
        // 对数据加密
        //Cipher cipher = new NullCipher();
        Cipher cipher = Cipher.getInstance("ECIES");
        cipher.init(Cipher.ENCRYPT_MODE, ecPrivateKey);
        return cipher.doFinal(data);
    }

    /**
     * @Description 使用公钥解密（私钥加密）
     * @author      daiyongbing
     * @param       publicKey 编码的公钥
     * @param       data 密文
     * @return      明文
     * @date        2019/1/24
     */
    public static byte[] decryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        // 对数据解密
        Cipher cipher = new NullCipher();
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = CertificateUtil.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        String data = "你好！";

        byte[] crpyt = encryptByPrivateKey(data.getBytes(), privateKey);
        System.out.println("加密后："+crpyt);
        System.out.println("解密后："+new String(decryptByPublicKey(crpyt, publicKey)));
    }
}
