package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderDo;

public interface OrderDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
}