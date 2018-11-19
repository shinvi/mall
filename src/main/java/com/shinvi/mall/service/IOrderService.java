package com.shinvi.mall.service;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.OrderVo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;

import java.util.List;

/**
 * @author 邱长海
 */
public interface IOrderService {

    QrCodeOrderVo payOrder(Integer userId, Long orderNo, Integer payType);

    OrderDo getQrCodeOrderStatus(Integer userId, String outTradeNo);

    void cancelOrderPayByOutTradeNo(Integer userId, String outTradeNo);

    OrderVo addOrderByProduct(Integer userId, Integer productId, Integer quantity, Integer shippingId);

    List<OrderVo> addOrdersByCarts(Integer userId, Integer[] cartIds, Integer shippingId);

    OrderDo cancelOrder(Integer userId, Long orderNo);

    PageInfo<OrderVo> getOrdersByConditions(Integer userId, int page, int pageSize, Integer status, Long orderNo);

    OrderDo shipOrder(Long orderNo);
}
