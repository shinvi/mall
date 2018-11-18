package com.shinvi.mall.service;

import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;

/**
 * @author 邱长海
 */
public interface IOrderService {

    QrCodeOrderVo payOrder(Integer userId, Long orderNo, Integer payType);

    OrderDo getQrCodeOrderStatus(Integer userId, String outTradeNo);

    void cancelOrderPayByOutTradeNo(Integer userId, String outTradeNo);

    OrderDo addOrderByProduct(Integer userId, Integer productId, Integer quantity, Integer shippingId);
}
