package com.shinvi.mall.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.base.service.BaseService;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.common.ResponseCode;
import com.shinvi.mall.dao.*;
import com.shinvi.mall.pojo.domain.*;
import com.shinvi.mall.pojo.vo.AlipayOrderVo;
import com.shinvi.mall.pojo.vo.OrderVo;
import com.shinvi.mall.pojo.vo.QrCodeOrderVo;
import com.shinvi.mall.service.IOrderService;
import com.shinvi.mall.util.BigDecimalUtils;
import com.shinvi.mall.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author 邱长海
 */
@Service
public class OrderService extends BaseService implements IOrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private OrderDoMapper orderDoMapper;

    @Autowired
    private OrderInfoDoMapper orderInfoDoMapper;

    @Autowired
    private OrderItemDoMapper orderItemDoMapper;

    @Autowired
    private ShippingDoMapper shippingDoMapper;

    @Autowired
    private ProductDoMapper productDoMapper;

    @Autowired
    private CartDoMapper cartDoMapper;


    /**
     * 支付下单,先去订单信息表查当前订单最后一次保存的支付流水号
     * 如果此流水号不存在,代表首次支付,直接请求对应支付平台接口下单
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

        List<OrderItemDo> orderItems = orderItemDoMapper.selectByOrderNo(order.getOrderNo());
        if (orderItems == null || orderItems.isEmpty()) {
            throw new ServerResponseException(ResponseCode.SERVER_ERROR.setDesc("订单信息不完整,请重新生成此订单后再支付"));
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

            return dispatchPayType(order, orderInfo, orderItems);

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
        if (order.getStatus() != Const.Order.STATUS_UNPAID) {
            return order;
        }
        switch (orderInfo.getPayType()) {
            case Const.Order.PAY_TYPE_ALIPAY:
                AlipayTradeQueryResponse queryResponse;
                try {
                    queryResponse = queryAlipayOrder(orderInfo.getOutTradeNo());
                } catch (Exception e) {
                    logger.error("订单支付状态查询失败", e);
                    throw new ServerResponseException(ResponseCode.PAY_STATUS_FAILED);
                }
                if (Const.AlipayCode.FAILED.equals(queryResponse.getCode()) &&
                        Const.AlipayCode.SUB_ACQ_TRADE_NOT_EXIST.equals(queryResponse.getSubCode())) {
                    throw new ServerResponseException(ResponseCode.PAY_STATUS_NOT_EXIST);
                }
                if (!Const.AlipayCode.SUCCESS.equals(queryResponse.getCode())) {
                    throw new ServerResponseException(ResponseCode.PAY_STATUS_FAILED);
                }
                if (Const.AlipayCode.QUERY_TRADE_FINISHED.equals(queryResponse.getTradeStatus()) ||
                        Const.AlipayCode.QUERY_TRADE_SUCCESS.equals(queryResponse.getTradeStatus())) {
                    order.setStatus(Const.Order.STATUS_PAID);
                    order.setPaymentTime(queryResponse.getSendPayDate());
                    if (orderDoMapper.updateByPrimaryKeySelective(order) <= 0) {
                        throw new ServerResponseException(ResponseCode.PAY_STATUS_EXCEPTION);
                    }
                }
                if (Const.AlipayCode.QUERY_WAIT_BUYER_PAY.equals(queryResponse.getTradeStatus())) {
                    throw new ServerResponseException(ResponseCode.PAY_STATUS_WATTING);
                }
                break;
            case Const.Order.PAY_TYPE_WECHAT:
                break;
            default:
                throw new ServerResponseException("无效的支付方式");
        }
        return order;
    }


    @Override
    public void cancelOrderPayByOutTradeNo(Integer userId, String outTradeNo) {
        OrderInfoDo orderInfo = orderInfoDoMapper.selectByOutTradeNo(outTradeNo);
        if (orderInfo == null) {
            throw new ServerResponseException("该支付流水号不存在");
        }
        OrderDo order = orderDoMapper.selectByUserIdNPrimaryKey(userId, orderInfo.getOrderNo());
        if (order == null) {
            throw new ServerResponseException("该订单不存在或您没有权限操作此订单");
        }
        if (order.getStatus() != Const.Order.STATUS_UNPAID) {
            throw new ServerResponseException("该订单已支付成功或已关闭");
        }
        try {
            dispatchCancelOldPayOrder(orderInfo);
        } catch (Exception e) {
            logger.error("订单支付取消失败", e);
            String msg = "订单支付取消失败";
            if (e instanceof ServerResponseException && ((ServerResponseException) e).getExtrasCode() != null) {
                msg = ((ServerResponseException) e).getExtrasCode().getDesc();
            }
            throw new ServerResponseException(msg);
        }
    }

    @Transactional
    @Override
    public OrderVo addOrderByProduct(Integer userId, Integer productId, Integer quantity, Integer shippingId) {
        return addOrderImpl(userId, productId, quantity, shippingId);
    }

    @Override
    public List<OrderVo> addOrdersByCarts(Integer userId, Integer[] cartIds, Integer shippingId) {
        List<CartDo> carts = cartDoMapper.selectByUserIdMPrimaryKeys(userId, cartIds);
        List<OrderVo> orders = new ArrayList<>();
        for (CartDo cart : carts) {
            TransactionStatus transaction = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
            try {
                OrderVo order = addOrderImpl(userId, cart.getProductId(), cart.getQuantity(), shippingId);
                if (cartDoMapper.deleteByPrimaryKey(cart.getId()) <= 0) {
                    throw new ServerResponseException("删除购物车失败");
                }
                transactionManager.commit(transaction);
                orders.add(order);
            } catch (Exception e) {
                logger.error("生成订单失败", e);
                transactionManager.rollback(transaction);
            }
        }
        return orders;
    }

    @Transactional
    @Override
    public OrderDo cancelOrder(Integer userId, Long orderNo) {
        OrderDo order = orderDoMapper.selectByUserIdNPrimaryKey(userId, orderNo);
        if (order == null) {
            throw new ServerResponseException("该订单不存在或您没有权限查看此订单");
        }
        if (order.getStatus() != Const.Order.STATUS_UNPAID) {
            throw new ServerResponseException("该订单不可取消");
        }
        order.setStatus(Const.Order.STATUS_CANCELLED);
        if (orderDoMapper.updateByPrimaryKeySelective(order) <= 0) {
            throw new ServerResponseException("取消失败");
        }
        return order;
    }

    @Override
    public PageInfo<OrderVo> getOrdersByConditions(Integer userId, int page, int pageSize, Integer status, Long orderNo) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(orderDoMapper.selectOrdersByConditions(userId, status, orderNo));
    }

    @Override
    public OrderDo shipOrder(Long orderNo) {
        OrderDo order = orderDoMapper.selectByPrimaryKey(orderNo);
        if (order == null) {
            throw new ServerResponseException("订单不存在");
        }
        if (order.getStatus() != Const.Order.STATUS_PAID) {
            throw new ServerResponseException("该订单已发货、未支付或已取消");
        }
        order.setStatus(Const.Order.STATUS_SHIPPED);
        order.setSendTime(new Date());
        if (orderDoMapper.updateByPrimaryKeySelective(order) <= 0) {
            throw new ServerResponseException("设置订单发货失败");
        }
        return order;
    }

    private OrderVo addOrderImpl(Integer userId, Integer productId, Integer quantity, Integer shippingId) {
        ShippingDo shipping = shippingDoMapper.selectByUserIdNPrimaryKey(shippingId, userId);
        if (shipping == null) {
            throw new ServerResponseException("收货地址不存在");
        }

        ProductDo product = productDoMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new ServerResponseException("商品不存在");
        }
        if (product.getStatus() != Const.Product.STATUS_IN_STOCK) {
            throw new ServerResponseException("商品已下线或已删除");
        }

        if (quantity > product.getStock()) {
            throw new ServerResponseException("商品库存不足");
        }
        product.setStock(product.getStock() - quantity);
        if (productDoMapper.updateByPrimaryKeySelective(product) <= 0) {
            throw new ServerResponseException("更新商品库存失败");
        }

        OrderItemDo orderItem = new OrderItemDo();
        orderItem.setUserId(userId);
        orderItem.setProductMainImage(product.getMainImage());
        orderItem.setProductName(product.getName());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(BigDecimalUtils.multiply(product.getPrice(), quantity));


        OrderDo order = new OrderDo();
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(Const.Order.STATUS_UNPAID);
        order.setPayment(orderItem.getTotalPrice());
        if (orderDoMapper.insert(order) <= 0) {
            throw new ServerResponseException("生成订单失败");
        }

        orderItem.setOrderNo(order.getOrderNo());
        if (orderItemDoMapper.insert(orderItem) <= 0) {
            throw new ServerResponseException("生成订单失败,保存订单商品信息失败");
        }
        OrderVo orderVo = new OrderVo(order);
        orderVo.setOrderItems(List.of(orderItem));
        orderVo.setShipping(shipping);
        return orderVo;
    }


    private QrCodeOrderVo dispatchPayType(OrderDo order, OrderInfoDo orderInfo, List<OrderItemDo> orderItems) throws
            Exception {
        QrCodeOrderVo qrcode = null;
        switch (orderInfo.getPayType()) {
            case Const.Order.PAY_TYPE_ALIPAY:
                qrcode = addAlipayOrder(order, orderInfo, orderItems);
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

    private QrCodeOrderVo addAlipayOrder(OrderDo order, OrderInfoDo orderAlipay, List<OrderItemDo> orderItems) throws
            Exception {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemDo item : orderItems) {
            totalPrice = BigDecimalUtils.add(totalPrice, item.getTotalPrice());
        }
        String productName = orderItems.get(0).getProductName() + "等" + orderItems.size() + "件商品";

        AlipayTradePrecreateRequest request = (AlipayTradePrecreateRequest) applicationContext.getBean("alipayTradePrecreateRequest");
        AlipayOrderVo alipayOrder = new AlipayOrderVo(orderAlipay.getOutTradeNo(),
                totalPrice.toString(), productName);
        request.setBizContent(jsonUtils.toJson(alipayOrder));

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
        query.setBizContent(jsonUtils.toJson(alipayOrderVo));
        return alipayClient.execute(query);
    }

    private AlipayTradeCancelResponse cancelAlipayOrder(String alipayTradeNo) throws Exception {
        AlipayTradeCancelRequest cancel = (AlipayTradeCancelRequest) applicationContext.getBean("alipayTradeCancelRequest");
        AlipayOrderVo alipayOrderVo = new AlipayOrderVo(alipayTradeNo);
        cancel.setBizContent(jsonUtils.toJson(alipayOrderVo));
        return alipayClient.execute(cancel);
    }

    private void handleCancelAlipayOrder(String alipayTradeNo) throws Exception {
        AlipayTradeCancelResponse response = cancelAlipayOrder(alipayTradeNo);
        if (!Const.AlipayCode.SUCCESS.equals(response.getCode())) {
            throw new ServerResponseException(response.getBody());
        }
    }
}
