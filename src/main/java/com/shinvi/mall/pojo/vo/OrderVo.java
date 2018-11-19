package com.shinvi.mall.pojo.vo;

import com.shinvi.mall.pojo.domain.OrderDo;
import com.shinvi.mall.pojo.domain.OrderItemDo;
import com.shinvi.mall.pojo.domain.ShippingDo;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author 邱长海
 */
public class OrderVo extends OrderDo {
    private List<OrderItemDo> orderItems;
    private ShippingDo shipping;

    public OrderVo(OrderDo order) {
        BeanUtils.copyProperties(order, this);
    }

    public OrderVo() {
    }

    public List<OrderItemDo> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDo> orderItems) {
        this.orderItems = orderItems;
    }

    public ShippingDo getShipping() {
        return shipping;
    }

    public void setShipping(ShippingDo shipping) {
        this.shipping = shipping;
    }
}
