package com.shinvi.mall.service;

import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;

/**
 * @author 邱长海
 */
public interface IOrderService {

    QrCodeOrderVo payOrder(Integer userId, Long orderNo, Integer payType);

    OrderDo getQrCodeOrderStatus(Integer userId, String outTradeNo);

    OrderDo addOrder(Integer userId, String products);
}
