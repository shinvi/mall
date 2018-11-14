package com.shinvi.mall.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.base.service.BaseService;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.common.ResponseCode;
import com.shinvi.mall.dao.OrderAlipayDoMapper;
import com.shinvi.mall.dao.OrderDoMapper;
import com.shinvi.mall.pojo.domain.OrderAlipayDo;
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

    @Autowired
    private OrderAlipayDoMapper orderAlipayDoMapper;

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
        OrderAlipayDo orderAlipay = orderAlipayDoMapper.selectByPrimaryKey(order.getOrderNo());
        if (orderAlipay == null) {
            if (orderAlipayDoMapper.insert(order.getOrderNo()) > 0) {
                orderAlipay = new OrderAlipayDo(order.getOrderNo(), order.getOrderNo() + "_0", 0);
            } else {
                throw new ServerResponseException("生成支付宝流水号失败,请5秒后重试");
            }
        }

        try {
            AlipayTradeQueryRequest query = (AlipayTradeQueryRequest) applicationContext.getBean("alipayTradeQueryRequest");

            if (orderAlipay.getN() != 0) {
                AlipayOrderVo alipayOrderVo = new AlipayOrderVo(orderAlipay.getAlipayTradeNo());
                query.setBizContent(objectMapper.writeValueAsString(alipayOrderVo));
                AlipayTradeQueryResponse queryResponse = alipayClient.execute(query);

                if (Const.AlipayCode.SUCCESS.equals(queryResponse.getCode())) {
                    if (Const.AlipayCode.QUERY_TRADE_CLOSED.equals(queryResponse.getTradeStatus())) {
                        return alipayOrderImpl(orderAlipay.getAlipayTradeNo(), order);
                    } else if (Const.AlipayCode.QUERY_TRADE_FINISHED.equals(queryResponse.getTradeStatus()) ||
                            Const.AlipayCode.QUERY_TRADE_SUCCESS.equals(queryResponse.getTradeStatus())) {
                        throw new ServerResponseException(queryResponse.getBody()).setExtrasCode(ResponseCode.ALIPAY_SUCCEDD);
                    } else if (Const.AlipayCode.QUERY_WAIT_BUYER_PAY.equals(queryResponse.getTradeStatus())) {
                        // TODO: 2018/11/14 撤销交易

                        return alipayOrderImpl(orderAlipay.getAlipayTradeNo(), order);
                    } else {
                        throw new ServerResponseException(queryResponse.getBody());
                    }
                } else if (Const.AlipayCode.FAILED.equals(queryResponse.getCode()) && Const.AlipayCode.SUB_ACQ_TRADE_NOT_EXIST.equals(queryResponse.getSubCode())) {
                    return alipayOrderImpl(orderAlipay.getAlipayTradeNo(), order);
                } else {
                    throw new ServerResponseException(queryResponse.getBody());
                }
            } else {
                return alipayOrderImpl(orderAlipay.getAlipayTradeNo(), order);
            }
        } catch (Exception e) {
            logger.error("支付宝下单失败", e);
            String msg = "支付宝下单失败,请5秒后重试";
            if (e instanceof ServerResponseException && ((ServerResponseException) e).getExtrasCode() != null) {
                msg = ((ServerResponseException) e).getExtrasCode().getDesc();
            }
            throw new ServerResponseException(msg);
        }
    }

    private String wechatOrder(OrderDo order) {
        return null;
    }

    private String alipayOrderImpl(String alipayTradeNo, OrderDo order) throws Exception {
        // TODO: 2018/11/14 查询orderItem信息
        AlipayTradePrecreateRequest request = (AlipayTradePrecreateRequest) applicationContext.getBean("alipayTradePrecreateRequest");
        request.setBizContent(objectMapper.writeValueAsString(new AlipayOrderVo(alipayTradeNo, "12599", "iphone xs max 256g")));
        AlipayTradePrecreateResponse precreateResponse = alipayClient.execute(request);
        if (precreateResponse.getCode().equals("10000")) {
            return precreateResponse.getQrCode();
        } else {
            throw new ServerResponseException(precreateResponse.getBody());
        }
    }
}
