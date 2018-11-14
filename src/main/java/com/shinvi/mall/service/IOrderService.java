package com.shinvi.mall.service;

/**
 * @author 邱长海
 */
public interface IOrderService {

    String payOrder(Integer userId, Long orderNo, Integer payType);
}
