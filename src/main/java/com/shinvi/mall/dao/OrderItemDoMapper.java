package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderItemDo;

public interface OrderItemDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItemDo record);

    int insertSelective(OrderItemDo record);

    OrderItemDo selectByPrimaryKey(Integer id);

    OrderItemDo selectByOrderNo(Long orderNo);

    int updateByPrimaryKeySelective(OrderItemDo record);

    int updateByPrimaryKey(OrderItemDo record);
}