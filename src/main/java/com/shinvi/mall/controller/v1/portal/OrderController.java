package com.shinvi.mall.controller.v1.portal;

import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.domain.OrderItemDo;
import com.shinvi.mall.pojo.domain.ShippingDo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IOrderService;
import com.shinvi.mall.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    }

    @ValidToken
    @RequestMapping(value = "/product/{" + Const.ID + "}", method = RequestMethod.POST)
    public ServerResponse<OrderDo> addOrderByProduct(@RequestAttribute(Const.User.USER_ID) Integer userId,
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
    public ServerResponse<OrderDo> addOrderByCart(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                  @PathVariable(Const.ID) String cartIds) {

    }
}
