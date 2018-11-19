package com.shinvi.mall.controller.v1.backend;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.ValidAdmin;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.vo.OrderVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IOrderService;
import com.shinvi.mall.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "manage/order", headers = "version=1.0")
public class OrderManageController {

    @Autowired
    private IOrderService orderService;

    @ValidAdmin
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo<OrderVo>> getOrders(@RequestParam(defaultValue = "1", required = false) int page,
                                                       @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                       Integer userId, Integer status, Long orderNo) {
        if (status != null && !ObjectUtils.in(status, Const.Order.STATUS_UNPAID, Const.Order.STATUS_CANCELLED, Const.Order.STATUS_PAID,
                Const.Order.STATUS_CLOSED, Const.Order.STATUS_FINISHED, Const.Order.STATUS_SHIPPED)) {
            return ServerResponse.error("订单状态非法");
        }
        return ServerResponse.success(orderService.getOrdersByConditions(userId, page, pageSize, status, orderNo));
    }

    @ValidAdmin
    @RequestMapping(value = "/ship", method = RequestMethod.PUT)
    public ServerResponse<OrderDo> shipOrder(Long orderNo) {
        if (orderNo == null) {
            return ServerResponse.error("订单不存在");
        }
        return ServerResponse.success(orderService.shipOrder(orderNo));
    }
}
