package com.iscas.workingdiarys.util.file;

import java.io.*;

/**
 * @Description:    序列化与反序列化工具类
 * @Author:         daiyongbing
 * @CreateDate:     2019/1/22
 * @Version:        1.0
 */
public class SerializeUtils {
    /**
     * Java 序列化方法
     * @param value
     * @return
     */
    public static byte[] serialise(Object value) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(stream);
            oos.writeObject(value);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    /**
     * Java 反序列化方法
     * @param bytes
     * @return
     */
    public static Object deserialise(byte[] bytes){
        ObjectInputStream ois = null;
        Object value = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            value = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
