package com.shinvi.mall.controller.v1.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.AlipayOrderVo;
import com.shinvi.mall.pojo.vo.OrderVo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IOrderService;
import com.shinvi.mall.util.JsonUtils;
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

    @Autowired
    private AlipayClient alipayClient;

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

    @Autowired
    private JsonUtils jsonUtils;

    @GetMapping(path = "/testAliPay")
    public ServerResponse<String> testAppAlipay() {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                "2019030463410883",
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC3iWG2YDqVX7PRfNQsBe8d23VolWhjiOAD9TouschmwR4/6OViI/ypw1OEml3Fi4+vtryQ2JomK9NGBMTlY4wci6IE2b5InpdVNEajXO0P/01RLpBdMqMv76aOsatRZ0e1JPIF8PSHBvTTNGqy6YZ7e8eYsZfwMljlt/XeyFIrhuxqflAeoPxJ9N8Qagzp6ivSv7UvHnJWtlmDP8FNIxaihxCZHh6hUmKaUnL/d5FWj3WvFcNidNnz1o4LMOKQwtxP0FQXf9dzlcYhdQfM9col47Iax5qWw8Zl1rmS5Qs0sKSkKqz3esLJgk68tIpDAQ0WReAsrx4rkqip2Iw6YKJtAgMBAAECggEAduJbU7oDWIx0Osbj3Fw/phRoecUFqCuaB5ZKCMMJ7106TeV659YtrH0kVSl0gihDe2sfu6dCOmV65cjjDT8tG7Rqzz+H3Rw1JHDHLLxdplYV6/cAdg393GgceCOnuAPxU+nJh4WrrucXoNM8JgXUIZptEIBAzcqfALXnYlZZu20CURbMWXgbg8WT2kuENq2TmEFz5falbcgGcB/oRVC6q06WXQ01mWQcvxLOn4A6ZTTFYzuJUlgyUaaOUtYOyAwOdNbFk8suq/+ZscDA9T4ZNzyKHt2UcbHF+rk+xmP1E17kwXKgNzx7XBg44TPyoa9HZ+tiYq5S0XAhqkQoD7CO1QKBgQDbycKRfTsd/cJ7qYYBddibyKCq8rjaxEL9ED2XpF6nTKE3nZ/j1p7lL5Aci/jZdECVTWwvQCDnqILpmca1Zv5qGXbiNqLgrIAaGtlqA/w8gw1vsBCYQeJx3kYFy2i4kHnqGvZymlf1ji3FeGzPynOxhI1h0UqrnXPulm5ja4P5XwKBgQDVxpqnGpucsuOstxsl8bJmZmrg3s53QIxEBNRXOvI7ALc6WMHPMTuudjnMH0GOyik7CzKkYw/gXEqN+JkHj/21ZHswXBwLyWq0IO8roMgEIIOAQEAB+uqI4I5DUB0lnau/2MSU6AOEmmpUnK0QzKp3Xlkn5cgZIviBSljwvlvbswKBgQCh07Zc5TYn6PXPk4xFev2YBmq2M60D9snUBqczBiVjTTrO9lIJsbagzRpwymbZxYzf4Xeseq0/pIVvdngmjMT6XfF4sf3PK3POxMIcGv8Xlu7Hs30kfi465QvtrE1Yx5fcVSnsS9RDywHMidSkUk5JAG4Q/cbbAT2hCEYTEdDERQKBgCYXs6sfJKdxgPxfzGigfuecTGMxkJzTVasxKR06FbvJztyIwFBJN5dI3devoiDtiRdKUYXVWRjjmS1fJPWiBNVzyDe/0gePpoHedH/TsJB7srMBv+Yabfxh9zeraOYxCE+xMqZOCYJxeY0GNhiOP7nxh/WCDnvcLWfsdZjAcuHdAoGBAIdZlJM2KYxB+pihZp/s+5rDfrEAxQpBH99o59ZPyfApFGA2hrzexJXVom2/6JL7gUEVmwJ1gDAy3LGeTr4QlwPaE0VPbaCVH6zVCRSQzDRwAxzgw19be3FdPgHICZxLpQzqxmJfNiPuDc5HLFbfIbvFBnTnun+3Dj5nu5RO8e57",
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApVm1GJ4pBBP/QPsdcT7Ne1j3T+X3U4SpuVC/Yf3VtOzI3ukk37+1kn60pt4ylmoXfBfc3GWlxK+iXSKwTWZHdFYG78VJI5qWs/2Qn7cx/7JbubPhWog6J53PnFwESg8hKkd7COujiCEnVWcc2weVpyGkZ2NOtmN2nU+vXmAFUgrYt4i2e14j7BHPWgULe04H8IlBd2Te19hNPI2FK61suxYw9kcB4dH2agw3d4LFZZrAyiUfNeVt/lKGZCcN6v4ryZr56Nfy0ORZkRMToJOHS764Wtyus4qO8ahP8jubXESVL+z/7ZaXRv24XRnjXYQDawHNly7Ef2ofpiqNKsYXWQIDAQAB",
                "RSA2");
        try {
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("我是测试数据");
            model.setTotalAmount("0.01");
            model.setSubject("APP支付测试");
            model.setOutTradeNo("2019030663464425");
            model.setTimeoutExpress("30m");
            model.setProductCode("QUICK_MSECURITY_PAY");
            request.setBizModel(model);
            AlipayTradeAppPayResponse execute = alipayClient.sdkExecute(request);
            return ServerResponse.success(jsonUtils.toJson(execute.getBody()));
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ServerResponse.error("cuowu");
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
