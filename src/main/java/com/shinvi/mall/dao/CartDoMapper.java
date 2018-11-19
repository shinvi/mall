package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.CartDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByPrimaryKeyNUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    int insert(CartDo record);

    int insertSelective(CartDo record);

    CartDo selectByPrimaryKey(Integer id);

    List<CartDo> selectByUserIdMPrimaryKeys(@Param("userId") Integer userId, @Param("ids") Integer[] ids);

    int updateByPrimaryKeySelective(CartDo record);

    int updateByPrimaryKey(CartDo record);

    CartDo selectByUserIdNProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<CartDo> selectALLByUserId(Integer userId);
}