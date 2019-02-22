package com.iscas.workingdiarys.service.impl;

import com.iscas.workingdiarys.entity.Cert;
import com.iscas.workingdiarys.entity.User;
import com.iscas.workingdiarys.mapper.CertMapper;
import com.iscas.workingdiarys.mapper.UserMapper;
import com.iscas.workingdiarys.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private CertMapper certMapper;

    /**
     * @Description 返回第一条满足条件的记录
     * @author      daiyongbing
     * @param       userName
     * @return      User
     * @date        2019/1/22
     */
    @Override
    public User selectOneByName(String userName) {

        return userMapper.selectOneByName(userName);
    }

    @Override
    public User selectOneById(String userId) {
        return userMapper.selectOneById(userId);
    }

    @Override
    public void register(User user, Cert cert) throws Exception{
        if (cert != null){
            certMapper.insert(cert);
        }
        userMapper.insert(user);
    }

    @Override
    public void updateUser(User user) throws Exception{
        if (user == null){
            new Exception("没有用户信息");
            return;
        }
        userMapper.update(user);
    }

    @Override
    public void changePassword(String userName, String newPassword) {
        userMapper.changePassword(userName, newPassword);
    }

    @Override
    public void putProof(String txid, String userName, String cipherText,  String privateKey, String publicKey) {
        userMapper.putProof(txid, userName, cipherText,  privateKey, publicKey);
    }

    @Override
    public boolean existUser(String userName) {
        if (userMapper.exist(userName) == null){
            return false;
        }else return true;
    }

    @Override
    public String getData(String txid) {
        if (txid==null || "".equals("")){
            new Exception("txid不能为空");
        }
        return userMapper.getData(txid);
    }
}
