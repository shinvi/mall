package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.ShippingDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingDoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUserIdNPrimaryKey(@Param("userId") Integer userId, @Param("id") Integer id);

    int insert(ShippingDo record);

    int insertSelective(ShippingDo record);

    ShippingDo selectByPrimaryKey(Integer id);

    ShippingDo selectByUserIdNPrimaryKey(@Param("id") Integer id, @Param("userId") Integer userId);

    List<ShippingDo> selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(ShippingDo record);

    int updateByUserIdNPrimaryKeySelective(ShippingDo record);

    int updateByPrimaryKey(ShippingDo record);

    int countByUserIdNPrimaryKey(@Param("id") Integer id, @Param("userId") Integer userId);
}