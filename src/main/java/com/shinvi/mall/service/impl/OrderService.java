package com.shinvi.mall.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.base.service.BaseService;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.OrderDoMapper;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.AlipayOrderVo;
import com.shinvi.mall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


/**
 * @author 邱长海
 */
@Service
public class OrderService extends BaseService implements IOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Override
    public String payOrder(Integer userId, Long orderNo, Integer payType) {
        OrderDo order = orderDoMapper.selectByUserIdNPrimaryKey(userId, orderNo);
        if (order == null) {
            throw new ServerResponseException("此订单不存在");
        }
        if (order.getStatus() != Const.Order.STATUS_UNPAID) {
            throw new ServerResponseException("此订单不可支付");
        }

        String qrcode;
        switch (payType) {
            case Const.Order.PAY_TYPE_ALIPAY:
                qrcode = alipayOrder(order);
                break;
            case Const.Order.PAY_TYPE_WECHAT:
                qrcode = wechatOrder(order);
                break;
            default:
                qrcode = "";
                break;
        }
        return qrcode;
    }

    private String alipayOrder(OrderDo order) {
        AlipayTradePrecreateRequest request = (AlipayTradePrecreateRequest) applicationContext.getBean("alipayRequest");
        AlipayOrderVo alipayOrder = new AlipayOrderVo(order.getOrderNo().toString(), "12599", "iphone xs max 256g");
        try {
            request.setBizContent(objectMapper.writeValueAsString(alipayOrder));
            AlipayTradePrecreateResponse execute = alipayClient.execute(request);
            if (execute.getCode().equals("10000")) {
                return execute.getQrCode();
            } else {
                throw new ServerResponseException(execute.getMsg());
            }
        } catch (Exception e) {
            logger.error("支付宝下单失败", e);
            throw new ServerResponseException("支付宝下单失败,请5秒后重试");
        }
    }

    private String wechatOrder(OrderDo order) {
        return null;
    }
}
