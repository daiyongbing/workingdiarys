package com.iscas.workingdiarys.service;

import com.iscas.workingdiarys.entity.Cert;
import com.iscas.workingdiarys.entity.User;

public interface UserService {
    User selectOneByName(String userName);

    User selectOneById(String userId);

    void register(User user, Cert cert) throws Exception;
}
