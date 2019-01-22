package com.iscas.workingdiarys.util.file;

import java.io.*;

/**
 * @Description:    文件操作工具类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/22
 * @Version:        1.0
 */
public class FileUtil {
    /**
     * @Description 读取文本文件到字符串
     * @author      daiyongbing
     * @param       filePath 文件路径
     * @return      String
     * @date        2019/1/22
     */
    public static String readStringFromFile(String filePath){
        String encoding = "UTF-8";
        File file = new File(filePath);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description 获取文件后缀名
     * @author      daiyongbing
     * @param       fileName 文件名
     * @return      ext 后缀名
     * @date        2019/1/22
     */
    public static String getFileExt(String fileName){
        int index =  fileName.lastIndexOf(".");
        String ext = fileName.substring(index);
        return ext;
    }

    /**
     * @Description 从输入流中获取字符串
     * @author      daiyongbing
     * @param       inputStream
     * @return      String
     * @date        2019/1/22
     */
    public static String getStringFromInputStream(InputStream inputStream){
        if (inputStream != null){
            try{
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine = new String("");
                while ((sTempOneLine = tBufferedReader.readLine()) != null){
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }


    public static void main(String[] args){
        System.out.println(getFileExt("gy.cer"));
    }
}
