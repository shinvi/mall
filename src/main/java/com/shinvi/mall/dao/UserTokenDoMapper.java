package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.UserTokenDo;

public interface UserTokenDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserTokenDo record);

    int insertSelective(UserTokenDo record);

    UserTokenDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserTokenDo record);

    int updateByPrimaryKey(UserTokenDo record);

    UserTokenDo selectByToken(String token);

    UserTokenDo selectByUserId(Integer userId);

    Integer selectUserIdByToken(String token);
}