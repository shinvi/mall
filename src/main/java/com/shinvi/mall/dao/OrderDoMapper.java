package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderDo;
import org.apache.ibatis.annotations.Param;

public interface OrderDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(Integer id);

    OrderDo selectByUserIdNPrimaryKey(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
}