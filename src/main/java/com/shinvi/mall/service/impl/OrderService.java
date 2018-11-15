package com.shinvi.mall.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.base.service.BaseService;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.common.ResponseCode;
import com.shinvi.mall.dao.OrderInfoDoMapper;
import com.shinvi.mall.dao.OrderDoMapper;
import com.shinvi.mall.dao.OrderItemDoMapper;
import com.shinvi.mall.pojo.domain.OrderInfoDo;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.domain.OrderItemDo;
import com.shinvi.mall.pojo.vo.AlipayOrderVo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;
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
    private OrderInfoDoMapper orderInfoDoMapper;

    @Autowired
    private OrderItemDoMapper orderItemDoMapper;


    /**
     * 下单,先去订单信息表查当前订单最后一次保存的支付流水号
     * 如果此流水号不存在,请求对应支付平台接口下单
     * 如果此流水号是正在等待支付的状态,撤消此流水,然后重新生成新的流水号请求对应平台接口下单
     * 如果此流水号是已关闭或者已退款状态,生成新的流水号请求对应平台接口下单
     * 如果此流水号是已付款结束,接口返回 {@link ResponseCode#PAY_SUCCEDD}
     *
     * @return 二维码内容
     */
    @Transactional
    @Override
    public QrCodeOrderVo payOrder(Integer userId, Long orderNo, Integer payType) {
        OrderDo order = orderDoMapper.selectByUserIdNPrimaryKey(userId, orderNo);
        if (order == null) {
            throw new ServerResponseException("此订单不存在");
        }
        if (order.getStatus() != Const.Order.STATUS_UNPAID) {
            throw new ServerResponseException("此订单不可支付");
        }

        OrderInfoDo orderInfo = orderInfoDoMapper.selectByPrimaryKey(order.getOrderNo());
        try {
            if (orderInfo != null) {
                dispatchCancelOldPayOrder(orderInfo);
                orderInfo.setPayType(payType);
                if (orderInfoDoMapper.updateByPrimaryKey(orderInfo) <= 0) {
                    throw new ServerResponseException("更新订单支付流水号失败");
                }
            } else {
                orderInfo = new OrderInfoDo(order.getOrderNo(), payType);
                if (orderInfoDoMapper.insert(orderInfo) <= 0) {
                    throw new ServerResponseException("生成订单支付流水号失败");
                }
            }
            orderInfo = orderInfoDoMapper.selectByPrimaryKey(order.getOrderNo());
            if (orderInfo == null) {
                throw new ServerResponseException("获取订单支付流水号失败");
            }

            return dispatchPayType(order, orderInfo);


        } catch (Exception e) {
            logger.error("订单支付失败", e);
            String msg = "订单支付失败,请5秒后重试";
            if (e instanceof ServerResponseException && ((ServerResponseException) e).getExtrasCode() != null) {
                msg = ((ServerResponseException) e).getExtrasCode().getDesc();
            }
            throw new ServerResponseException(msg);
        }

    }

    @Transactional
    @Override
    public OrderDo getQrCodeOrderStatus(Integer userId, String outTradeNo) {
        OrderInfoDo orderInfo = orderInfoDoMapper.selectByOutTradeNo(outTradeNo);
        if (orderInfo == null) {
            throw new ServerResponseException("该支付流水号不存在");
        }
        OrderDo order = orderDoMapper.selectByUserIdNPrimaryKey(userId, orderInfo.getOrderNo());
        if (order == null) {
            throw new ServerResponseException("该订单不存在或您没有权限查看此订单");
        }
        if (order.getStatus() == Const.Order.STATUS_UNPAID) {
            try {
                AlipayTradeQueryResponse queryResponse = queryAlipayOrder(orderInfo.getOutTradeNo());
            } catch (Exception e) {
                logger.error("订单支付状态查询失败", e);
                throw new ServerResponseException("订单支付状态查询失败,请稍后再试");
            }
        }
        return order;
    }

    private QrCodeOrderVo dispatchPayType(OrderDo order, OrderInfoDo orderInfo) throws Exception {
        QrCodeOrderVo qrcode = null;
        switch (orderInfo.getPayType()) {
            case Const.Order.PAY_TYPE_ALIPAY:
                qrcode = addAlipayOrder(order, orderInfo);
                break;
            case Const.Order.PAY_TYPE_WECHAT:
                break;
        }
        return qrcode;
    }

    private void dispatchCancelOldPayOrder(OrderInfoDo orderInfo) throws Exception {
        switch (orderInfo.getPayType()) {
            case Const.Order.PAY_TYPE_ALIPAY:
                AlipayTradeQueryResponse queryResponse = queryAlipayOrder(orderInfo.getOutTradeNo());
                if (!Const.AlipayCode.SUCCESS.equals(queryResponse.getCode()) &&
                        !Const.AlipayCode.SUB_ACQ_TRADE_NOT_EXIST.equals(queryResponse.getSubCode())) {
                    throw new ServerResponseException(queryResponse.getBody());
                }
                if (Const.AlipayCode.QUERY_TRADE_FINISHED.equals(queryResponse.getTradeStatus()) ||
                        Const.AlipayCode.QUERY_TRADE_SUCCESS.equals(queryResponse.getTradeStatus())) {
                    throw new ServerResponseException(queryResponse.getBody()).setExtrasCode(ResponseCode.PAY_SUCCEDD);
                }
                if (Const.AlipayCode.QUERY_WAIT_BUYER_PAY.equals(queryResponse.getTradeStatus())) {
                    handleCancelAlipayOrder(orderInfo.getOutTradeNo());
                }
                break;
            case Const.Order.PAY_TYPE_WECHAT:
                break;
        }
    }

    private QrCodeOrderVo addAlipayOrder(OrderDo order, OrderInfoDo orderAlipay) throws Exception {
        OrderItemDo orderItem = orderItemDoMapper.selectByOrderNo(order.getOrderNo());
        if (orderItem == null) {
            throw new ServerResponseException(ResponseCode.SERVER_ERROR.setDesc("订单信息不完整,请重新生成此订单后再支付"));
        }
        AlipayTradePrecreateRequest request = (AlipayTradePrecreateRequest) applicationContext.getBean("alipayTradePrecreateRequest");
        AlipayOrderVo alipayOrder = new AlipayOrderVo(orderAlipay.getOutTradeNo(),
                orderItem.getTotalPrice().toString(), orderItem.getProductName());
        request.setBizContent(objectMapper.writeValueAsString(alipayOrder));
        AlipayTradePrecreateResponse precreateResponse = alipayClient.execute(request);
        if (Const.AlipayCode.SUCCESS.equals(precreateResponse.getCode())) {
            String qrcode = precreateResponse.getQrCode();
            return new QrCodeOrderVo(qrcode, alipayOrder.getTimeout_express(),
                    Const.Order.PAY_TYPE_ALIPAY, order.getOrderNo(), orderAlipay.getOutTradeNo());
        } else {
            throw new ServerResponseException(precreateResponse.getBody());
        }
    }

    private AlipayTradeQueryResponse queryAlipayOrder(String alipayTradeNo) throws Exception {
        AlipayTradeQueryRequest query = (AlipayTradeQueryRequest) applicationContext.getBean("alipayTradeQueryRequest");
        AlipayOrderVo alipayOrderVo = new AlipayOrderVo(alipayTradeNo);
        query.setBizContent(objectMapper.writeValueAsString(alipayOrderVo));
        return alipayClient.execute(query);
    }

    private AlipayTradeCancelResponse cancelAlipayOrder(String alipayTradeNo) throws Exception {
        AlipayTradeCancelRequest cancel = (AlipayTradeCancelRequest) applicationContext.getBean("alipayTradeCancelRequest");
        AlipayOrderVo alipayOrderVo = new AlipayOrderVo(alipayTradeNo);
        cancel.setBizContent(objectMapper.writeValueAsString(alipayOrderVo));
        return alipayClient.execute(cancel);
    }

    private void handleCancelAlipayOrder(String alipayTradeNo) throws Exception {
        AlipayTradeCancelResponse response = cancelAlipayOrder(alipayTradeNo);
        if (!Const.AlipayCode.SUCCESS.equals(response.getCode())) {
            throw new ServerResponseException(response.getBody());
        }
    }
}
