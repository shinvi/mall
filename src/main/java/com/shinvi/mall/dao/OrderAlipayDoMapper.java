package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.OrderAlipayDo;

/**
 * @author 邱长海
 */
public interface OrderAlipayDoMapper {
    int deleteByPrimaryKey(Long orderNo);

    int insert(OrderAlipayDo orderAlipay);

    int insertSelective(OrderAlipayDo orderAlipay);

    OrderAlipayDo selectByPrimaryKey(Long orderNo);

    OrderAlipayDo selectByAlipayTradeNo(String alipayTradeNo);

    int updateByPrimaryKeySelective(OrderAlipayDo orderAlipay);

    int updateByPrimaryKey(OrderAlipayDo orderAlipay);
}
