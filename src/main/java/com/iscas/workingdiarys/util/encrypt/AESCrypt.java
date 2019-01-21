package com.iscas.workingdiarys.util.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Description:    AES解密/解密工具类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/21
 * @Version:        1.0
 */
public class AESCrypt {
    /**
     * AES加密（密钥为任意长度）
     * @author      daiyongbing
     * @param       src
     * @param       password
     * @return      byte[]
     * @throws      Exception
     * @date        2019/1/21
     */
    public static byte[] encrypt(byte[] src, String password) throws RuntimeException {
        String md5Key = MD5Utils.stringSub16Md5(password);
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = md5Key.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = null;
        byte[] aesEncrypted = null;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            aesEncrypted = cipher.doFinal(src);
        } catch (NoSuchAlgorithmException e) {
            new NoSuchAlgorithmException("NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            new InvalidKeyException("InvalidKeyException");
        } catch (NoSuchPaddingException e) {
            new NoSuchPaddingException("NoSuchPaddingException");
        } catch (BadPaddingException e) {
            new BadPaddingException("BadPaddingException");
        } catch (IllegalBlockSizeException e) {
            new IllegalBlockSizeException("IllegalBlockSizeException");
        }
        return aesEncrypted;
    }

    /**
     * 功能：AES解密
     * @author      daiyongbing
     * @param       password
     * @return      解密后的字符串
     * @exception
     * @date        2019/1/21
     */
    public static byte[] decrypt(byte[] encryptedBytes, String password) throws RuntimeException{
        String md5Key = MD5Utils.stringSub16Md5(password);
        byte[] original = null;
        try {
            byte[] keyBytes = md5Key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            original = cipher.doFinal(encryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            new NoSuchAlgorithmException("NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            new InvalidKeyException("InvalidKeyException");
        } catch (NoSuchPaddingException e) {
            new NoSuchPaddingException("NoSuchPaddingException");
        } catch (BadPaddingException e) {
            new BadPaddingException("BadPaddingException");
        } catch (UnsupportedEncodingException e) {
            new UnsupportedEncodingException("UnsupportedEncodingException");
        } catch (IllegalBlockSizeException e) {
            new IllegalBlockSizeException("IllegalBlockSizeException");
        }
        return original;
    }


    public static void main(String[] args) throws Exception {
        String source = "hello";
        String password = "nklvnd54";
       // 加密
        byte[] encrypt = AESCrypt.encrypt(source.getBytes("utf-8"), password);
        System.out.println("加密后："+encrypt);

        String decrypt = new String(AESCrypt.decrypt(encrypt, password),"utf-8");
        System.out.println("解密后："+decrypt);
    }
}
