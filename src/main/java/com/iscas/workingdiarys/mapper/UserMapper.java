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

    CustomUserDetails findByUserName(@Param("userName") String userName);

    void insert(User user);

    void update(User user);

    void changePassword(@Param("userName") String userName, @Param("newPassword") String newPassword);


    /************************以下接口纯属测试，没什么卵用********************/

    void putProof(@Param("txid") String txid, @Param("userName") String userName, @Param("cipherText") String cipherText, @Param("privateKey") String privateKey, @Param("publicKey") String publicKey);

    String exist(@Param("userName") String userName);

    String getData(@Param("txid") String txid);
}
