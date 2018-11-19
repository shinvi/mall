package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.OrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDoMapper {
    int deleteByPrimaryKey(Long orderNo);

    int insert(OrderDo record);

    int insertSelective(OrderDo record);

    OrderDo selectByPrimaryKey(Long orderNo);

    OrderDo selectByUserIdNPrimaryKey(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    List<OrderVo> selectOrdersByConditions(@Param("userId") Integer userId, @Param("status") Integer status,@Param("orderNo") Long orderNo);

    int updateByPrimaryKeySelective(OrderDo record);

    int updateByPrimaryKey(OrderDo record);
}