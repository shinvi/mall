package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.CartDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartDo record);

    int insertSelective(CartDo record);

    CartDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartDo record);

    int updateByPrimaryKey(CartDo record);

    CartDo selectByUserIdNProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<CartDo> selectALLByUserId(Integer userId);
}