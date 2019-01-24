package com.iscas.workingdiarys.util.certificate;

import com.iscas.workingdiarys.util.encrypt.AESCrypt;
import com.iscas.workingdiarys.util.encode.Base64Util;
import com.iscas.workingdiarys.util.encrypt.MD5Utils;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Random;

/**
 * @Description:    java.security.certificate工具类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/22
 * @Version:        1.0
 */
public class CertificateUtil {
    private final static String ALGORITHM_CURVE_SECP256K1 = "secp256k1";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @Description 生成KeyPair
     * @Description 椭圆曲线：SECP256K1
     * @Description provider：EC
     * @author      daiyongbing
     * @return      KeyPair
     * @date        2019/1/22
     */
    public static KeyPair generateKeyPair(){
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(ALGORITHM_CURVE_SECP256K1);
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println(e.getMessage());
        }
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * @Description 生成X509证书
     * @Description SignatureAlgorithm -> SHA256WithECDSA
     * @Description provider -> BC
     * @author      daiyongbing
     * @param       certInfo String[8] = {CN:"Common Name", OU:"Organisation Unit", O:"Organisation Name", C:"Country", L:"Locality Name", ST:""State Name}
     * @param       keyPair
     * @throws      InvalidKeyException
     * @throws      NoSuchProviderException
     * @throws      SecurityException
     * @throws      SignatureException
     * @return      X509Certificate
     * @date        2019/1/22
     */
    public static X509Certificate generateCert(String[] certInfo, KeyPair keyPair) {
        X509V3CertificateGenerator cerGenerator = new X509V3CertificateGenerator();
        X509Certificate cert = null;
        cerGenerator.setSerialNumber(new BigInteger(256, new Random()));  // 序列号
        cerGenerator.setIssuerDN(new X509Name("CN="+certInfo[0]+", OU="+certInfo[1]+", O="+certInfo[2]+" , C="+certInfo[3]));
        cerGenerator.setNotBefore(new Date(System.currentTimeMillis()));
        cerGenerator.setNotAfter(new Date((System.currentTimeMillis()+365*24*60*60*1000L)));
        cerGenerator.setSubjectDN(new X509Name("CN=" + certInfo[0] + ",OU=" + certInfo[1] + ",O=" + certInfo[2] + ",C=" + certInfo[3] + ",L="
                + certInfo[4] + ",ST=" + certInfo[5]));
        cerGenerator.setPublicKey(keyPair.getPublic());
        //certificateGenerator.setSignatureAlgorithm("SHA1WithRSA");
        cerGenerator.setSignatureAlgorithm("SHA256WithECDSA");
        try {
            cert = cerGenerator.generateX509Certificate(keyPair.getPrivate(), "BC");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return cert;
    }

    /**
     * @Description 将Certificate转换成pem字符串
     * @author      daiyongbing
     * @param       certificate
     * @return      pemCertString
     * @date        2019/1/22
     */
    public static String getPemCertString(Certificate certificate){
        String encode = null;
        try {
            encode = Base64Util.encode2String(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        StringBuffer stringBuffer =  new StringBuffer(encode);
        int index;
        for (index=64; index<stringBuffer.length(); index+=65){
            stringBuffer.insert(index, "\n");
        }
        String pemCert = "-----BEGIN CERTIFICATE-----\r\n" + stringBuffer + "\r\n-----END CERTIFICATE-----\r\n";
        return pemCert;
    }

    /**
     * @Description 将Certificate序列化为pem格式后保存到文件系统文件
     * @author      daiyongbing
     * @param       certificate
     * @param       path    存储路径
     * @param       certName    文件名
     * @date        2019/1/22
     */
    public static void savePemCertAsFile(Certificate certificate, String path, String certName){
        String encodedCert = null;
        try {
            encodedCert = Base64Util.encode2String(certificate.getEncoded());
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(path+"/"+certName+".cer");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN CERTIFICATE-----\n".getBytes());
            int i = 0;
            for (; i<(encodedCert.length() - (encodedCert.length() % 64)); i+=64) {
                randomAccessFile.write(encodedCert.substring(i, i + 64).getBytes());
                randomAccessFile.write("\n".getBytes());
            }
            randomAccessFile.write(encodedCert.substring(i, encodedCert.length()).getBytes());
            randomAccessFile.write("\n".getBytes());
            randomAccessFile.write("-----END CERTIFICATE-----".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 将PemCert反序列化为Certificate对象
     * @author      daiyongbing
     * @param       pemcert
     * @return      Certificate
     * @date        2019/1/22
     */
    public static Certificate getCertFromPemString (String pemcert) {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        Certificate cert = null;
        try {
            cert = cf.generateCertificate(new ByteArrayInputStream(pemcert.getBytes()));
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return cert;
    }

    /**
     * @Description 将私钥转换成PEMString
     * @author      daiyongbing
     * @param       privateKey
     * @return      pemPrivateKeyString
     * @date        2019/1/22
     */
    public static String getPemFromPrivateKey(PrivateKey privateKey) {
        String pemPrivateKeyString = null;
        try {
            String encode = Base64Util.encode2String(privateKey.getEncoded());
            pemPrivateKeyString = "-----BEGIN PRIVATEKEY-----\r\n" + encode + "\r\n-----END PRIVATEKEY-----\r\n";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getPemFromPrivateKey:" + e.getMessage());
        }
        return pemPrivateKeyString;
    }

    /**
     * @Description pemPrivateKey得到PrivateKey对象
     * @author      daiyongbing
     * @param       pemPrivateKey
     * @param       algorithm
     * @return      PrivateKey
     * @date        2019/1/22
     */
    public static PrivateKey loadECPrivateKey(String pemPrivateKey,  String algorithm) throws Exception {
        String privateKeyStr = pemPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "").replace("", "");
        byte[] asBytes = Base64Util.decode2Bytes(privateKeyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(asBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(spec);
    }

    /**
     * @Description pemPublicKey转为PublicKey对象
     * @author      daiyongbing
     * @param       pemPublicKey
     * @param       algorithm
     * @return      PublicKey
     * @date        2019/1/22
     */
    public static PublicKey loadECPublicKey(String pemPublicKey,  String algorithm) throws Exception {
        String strPublicKey = pemPublicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "").replace("", "");
        byte[] asBytes = Base64Util.decode2Bytes(strPublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(asBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(spec);
    }

    /**
     * @Description 生成带密码的空jks
     * @author      daiyongbing
     * @param       certInfo {cn,ou,o,c,l,st,starttime,endtime,serialnumber}
     * @param       jks_password
     * @param       path jks的存储路径（目录）
     * @date        2019/1/22
     */
    public static void generateNullJKS(String[] certInfo, String jks_password, String path) {
        String jks_path = path+"/" + certInfo[0] + ".jks";
        File file = new File(jks_path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, jks_password.toCharArray());
            keyStore.store(new FileOutputStream(jks_path), jks_password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 将证书加入已有的空jks
     * @author      daiyongbing
     * @param       cert 证书
     * @param       keyPair 密钥对
     * @param       jks_password jks密码
     * @param       path jks目录
     * @param       commonName
     * @date        2019/1/22
     */
    public static void addCert2JKS(X509Certificate cert, KeyPair keyPair, String jks_password, String path, String commonName) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(path+"/"+commonName+".jks"), jks_password.toCharArray());

            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            keyStore.setCertificateEntry(commonName, cert);
            keyStore.setKeyEntry(commonName, keyPair.getPrivate(), jks_password.toCharArray(), chain);
            keyStore.store(new FileOutputStream(path+"/" + commonName + ".jks"), jks_password.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 创建jks,同时加入Certificate
     * @author      daiyongbing
     * @param cert
     * @param keyPair
     * @param jks_password
     * @param path
     * @param alias
     * @date        2019/1/22
     */
    public static void generateJksWithCert(X509Certificate cert, KeyPair keyPair, String jks_password, String path, String alias){
        KeyStore keyStore;
        String jks_path = path + "/" + alias + ".jks";
        File file = new File(jks_path);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, jks_password.toCharArray());
            X509Certificate[] chain = new X509Certificate[1];
            chain[0] = cert;
            keyStore.setCertificateEntry(alias, cert);
            keyStore.setKeyEntry(alias, keyPair.getPrivate(), jks_password.toCharArray(), chain);
            keyStore.store(new FileOutputStream(jks_path), jks_password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description 使用AES算法对证书私钥加密
     * @author      daiyongbing
     * @param       privateKey 私钥
     * @param       password 加密密码
     * @return      encyptPrivateKey 加密后的字符串
     * @date        2019/1/22
     */
    public static String encryptPrivateKey(PrivateKey privateKey, String password){
        String pemPrivateKey = CertificateUtil.getPemFromPrivateKey(privateKey); //获取pemKey
        byte[] privateKeyBytes = null;
        try {
            privateKeyBytes = AESCrypt.encrypt(pemPrivateKey.getBytes(), MD5Utils.stringSub16Md5(password)); //AES加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64Util.encode2String(privateKeyBytes);   // base64编码成字符串
    }

    /**
     * @Description 解密AES加密后的私钥
     * @author      daiyongbing
     * @param       encryptPrivateKey 加密后的私钥
     * @param       password 解密密码
     * @return      PrivateKey
     * @date        2019/1/22
     */
    public static PrivateKey decryptPrivateKey(String encryptPrivateKey, String password) {
        byte[] privateKeyBytes = Base64Util.decode2Bytes(encryptPrivateKey);  // base64解码
        byte[] pemPrivateKeyBytes = AESCrypt.decrypt(privateKeyBytes, MD5Utils.stringSub16Md5(password)); //AES解密
        String pemPrivateKey = null;
        PrivateKey privateKey = null;
        try {
            pemPrivateKey = new String(pemPrivateKeyBytes, "utf-8");
            privateKey = CertificateUtil.loadECPrivateKey(pemPrivateKey, "EC");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    /**
     * 读取私钥
     * @param encodedKey
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey loadPrivateKey(byte[] encodedKey,  String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(keySpec);
    }


    /**
     * 从字符串中提取证书
     * @param pemCert
     * @return
     */
    public static X509Certificate getCertificateFromPem(String pemCert) {
        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            java.io.ByteArrayInputStream streamCertificate = new java.io.ByteArrayInputStream(
                    pemCert.getBytes("UTF-8"));
            return (X509Certificate) certificateFactory.generateCertificate(streamCertificate);
        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }
        return null;
    }


    /**
     * 序列化公钥
     * @param publicKey
     * @param name
     * @throws Exception
     */
    public static void savePublicKeyAsPEM(PublicKey publicKey, String name) throws Exception {
        String content = Base64Util.encode2String(publicKey.getEncoded());
        File file = new File(name);
        if ( file.isFile() && file.exists() )
            throw new IOException("file already exists");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN PUBLIC KEY-----".getBytes());
            int i = 0;
            for (; i<(content.length() - (content.length() % 64)); i+=64) {
                randomAccessFile.write(content.substring(i, i + 64).getBytes());
                //randomAccessFile.write('');
                randomAccessFile.write("\n".getBytes());
            }

            randomAccessFile.write(content.substring(i, content.length()).getBytes());
            randomAccessFile.write("\n".getBytes());

            randomAccessFile.write("-----END PUBLIC KEY-----".getBytes());
        }
    }

    /**
     * 序列化私钥
     * @param privateKey
     * @param name
     * @throws Exception
     */
    public static void savePrivateKeyAsPEM(PrivateKey privateKey, String name) throws Exception {
        // String content = Base64.encode(privateKey.getEncoded());
        String content = Base64Util.encode2String(privateKey.getEncoded());
        File file = new File(name);
        if ( file.isFile() && file.exists() )
            throw new IOException("file already exists");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            randomAccessFile.write("-----BEGIN PRIVATE KEY-----".getBytes());
            int i = 0;
            for (; i<(content.length() - (content.length() % 64)); i+=64) {
                randomAccessFile.write(content.substring(i, i + 64).getBytes());
                //randomAccessFile.write('');
                randomAccessFile.write("\n".getBytes());
            }

            randomAccessFile.write(content.substring(i, content.length()).getBytes());
            //randomAccessFile.write('');
            randomAccessFile.write("\n".getBytes());

            randomAccessFile.write("-----END PRIVATE KEY-----".getBytes());
        }
    }


    /**
     * 读取公钥, encodedKey为从文件中读取到的byte[]数组
     * @param encodedKey
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey loadPublicKey(byte[] encodedKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(keySpec);
    }


    /**
     * readBytes代码
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(final InputStream inputStream) throws IOException {
        final int BUFFER_SIZE = 1024;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int readCount;
        byte[] data = new byte[BUFFER_SIZE];
        while ((readCount = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, readCount);
        }

        buffer.flush();
        return buffer.toByteArray();
    }
}
