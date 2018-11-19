package com.shinvi.mall.controller.v1.portal;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.OrderVo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IOrderService;
import com.shinvi.mall.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "order", headers = "version=1.0")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @ValidToken
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse<OrderDo> cancelOrder(@RequestAttribute(Const.User.USER_ID) Integer userId, Long orderNo) {
        if (orderNo == null) {
            return ServerResponse.error("订单编号不能为空");
        }
        return ServerResponse.success(orderService.cancelOrder(userId, orderNo));
    }


    @ValidToken
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo<OrderVo>> getOrders(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                       @RequestParam(defaultValue = "1", required = false) int page,
                                                       @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                       Integer status) {
        if (status != null && !ObjectUtils.in(status, Const.Order.STATUS_UNPAID, Const.Order.STATUS_CANCELLED, Const.Order.STATUS_PAID,
                Const.Order.STATUS_CLOSED, Const.Order.STATUS_FINISHED, Const.Order.STATUS_SHIPPED)) {
            return ServerResponse.error("订单状态非法");
        }
        return ServerResponse.success(orderService.getOrdersByConditions(userId, page, pageSize, status, null));
    }

    @ValidToken
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ServerResponse<QrCodeOrderVo> payQrCodeOrder(@RequestAttribute(Const.User.USER_ID) Integer userId, Long orderNo, Integer payType) {
        if (orderNo == null) {
            return ServerResponse.error("订单编号不能为空");
        }

        if (!ObjectUtils.in(payType, Const.Order.PAY_TYPE_ALIPAY, Const.Order.PAY_TYPE_WECHAT)) {
            return ServerResponse.error("非法的支付方式");
        }

        return ServerResponse.success(orderService.payOrder(userId, orderNo, payType));
    }

    @ValidToken
    @RequestMapping(value = "/pay/{" + Const.Order.OUT_TRADE_NO + "}", method = RequestMethod.GET)
    public ServerResponse<OrderDo> getQrCodeOrderPayStatus(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                           @PathVariable(Const.Order.OUT_TRADE_NO) String outTradeNo) {
        return ServerResponse.success(orderService.getQrCodeOrderStatus(userId, outTradeNo));
    }

    @ValidToken
    @RequestMapping(value = "/pay/{" + Const.Order.OUT_TRADE_NO + "}", method = RequestMethod.DELETE)
    public ServerResponse cancelOrderPayByOutTradeNo(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                     @PathVariable(Const.Order.OUT_TRADE_NO) String outTradeNo) {
        orderService.cancelOrderPayByOutTradeNo(userId, outTradeNo);
        return ServerResponse.success();
    }

    @ValidToken
    @RequestMapping(value = "/product/{" + Const.ID + "}", method = RequestMethod.POST)
    public ServerResponse<OrderVo> addOrderByProduct(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                     @PathVariable(Const.ID) Integer productId,
                                                     Integer quantity, Integer shippingId) {
        if (quantity == null || quantity <= 0) {
            return ServerResponse.error("商品数量必须大于0");
        }
        if (shippingId == null) {
            return ServerResponse.error("收货地址不能为空");
        }
        return ServerResponse.success(orderService.addOrderByProduct(userId, productId, quantity, shippingId));
    }


    @ValidToken
    @RequestMapping(value = "/cart/{" + Const.ID + "}", method = RequestMethod.POST)
    public ServerResponse<List<OrderVo>> addOrderByCart(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                        @PathVariable(Const.ID) String cartId, Integer shippingId) {
        if (shippingId == null) {
            return ServerResponse.error("收货地址不能为空");
        }
        if (StringUtils.isBlank(cartId)) {
            return ServerResponse.error("请选择待结算的购物车");
        }
        String[] cartIds = cartId.split(",");
        if (cartIds.length <= 0) {
            return ServerResponse.error("请选择待结算的购物车");
        }
        return ServerResponse.success(orderService.addOrdersByCarts(userId,
                Arrays.stream(cartIds).map(Integer::valueOf).toArray(Integer[]::new), shippingId));
    }
}
