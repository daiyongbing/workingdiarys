package com.iscas.workingdiarys.service.impl;

import com.iscas.workingdiarys.entity.User;
import com.iscas.workingdiarys.mapper.UserMapper;
import com.iscas.workingdiarys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User selectOne(String userName) {
        return userMapper.selectOne(userName);
    }
}
