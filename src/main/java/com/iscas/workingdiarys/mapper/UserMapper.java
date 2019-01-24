package com.iscas.workingdiarys.mapper;

import com.iscas.workingdiarys.entity.CustomUserDetails;
import com.iscas.workingdiarys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectOneByName(@Param("userName") String username);

    User selectOneById(@Param("userId") String userId);

    List<User> selectList(@Param("userName") String username);

    CustomUserDetails findByUserName(@Param("userName") String username);

    void insert(User user);
}
