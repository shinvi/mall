package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.CartDo;

public interface CartDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartDo record);

    int insertSelective(CartDo record);

    CartDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartDo record);

    int updateByPrimaryKey(CartDo record);
}