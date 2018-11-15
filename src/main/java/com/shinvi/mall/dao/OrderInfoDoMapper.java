package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderInfoDo;

/**
 * @author 邱长海
 */
public interface OrderInfoDoMapper {
    int deleteByPrimaryKey(Long orderNo);

    int insert(OrderInfoDo orderInfo);

    OrderInfoDo selectByPrimaryKey(Long orderNo);

    OrderInfoDo selectByOutTradeNo(String outTradeNo);

    int updateByPrimaryKey(OrderInfoDo orderInfo);
}
