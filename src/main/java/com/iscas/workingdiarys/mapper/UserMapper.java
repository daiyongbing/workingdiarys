package com.iscas.workingdiarys.mapper;

import com.iscas.workingdiarys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectOne(@Param("userName") String username);
}
