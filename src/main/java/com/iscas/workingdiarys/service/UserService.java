package com.iscas.workingdiarys.service;

import com.iscas.workingdiarys.entity.User;

public interface UserService {
    User selectOneByName(String userName);

    User selectOneById(String userId);
}
