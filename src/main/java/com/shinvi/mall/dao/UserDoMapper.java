package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.UserDo;

public interface UserDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKey(UserDo record);

    int countUsername(String username);

    int countEmail(String email);

    UserDo selectLogin(String username, String password);

    UserDo selectByToken(String token);
}