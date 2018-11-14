package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderAlipayDo;

/**
 * @author 邱长海
 */
public interface OrderAlipayDoMapper {
    int deleteByPrimaryKey(Long orderNo);

    int insert(Long orderNo);

    OrderAlipayDo selectByPrimaryKey(Long orderNo);

    OrderAlipayDo selectByAlipayTradeNo(String alipayTradeNo);

    int updateByPrimaryKey(Long orderNo);
}
