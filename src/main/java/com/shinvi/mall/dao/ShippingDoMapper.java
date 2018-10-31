package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.ShippingDo;

public interface ShippingDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShippingDo record);

    int insertSelective(ShippingDo record);

    ShippingDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShippingDo record);

    int updateByPrimaryKey(ShippingDo record);
}