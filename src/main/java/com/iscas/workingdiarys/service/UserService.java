package com.iscas.workingdiarys.service;

import com.iscas.workingdiarys.entity.Cert;
import com.iscas.workingdiarys.entity.User;

public interface UserService {
    User selectOneByName(String userName);

    User selectOneById(String userId);

    void register(User user, Cert cert) throws Exception;

    void updateUser(User user) throws Exception;

    void changePassword(String userName, String newPassword);


    //******************************* 以下所有方法都是测试授权解密 ***********************************/
    // 模拟数据上链，只是测试
    void putProof(String txid, String userName, String cipherText,  String privateKey, String publicKey);

    boolean existUser(String userName);

    String getData(String txid);
}
