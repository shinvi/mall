package com.shinvi.mall.service;

import com.shinvi.mall.pojo.domain.ShippingDo;

import java.util.List;

/**
 * @author 邱长海
 */
public interface IShippingService {

    ShippingDo addShipping(Integer userId, ShippingDo shipping);

    int deleteShippingByIdNUserId(Integer userId, Integer id);

    ShippingDo updateShipping(Integer userId, ShippingDo shipping);

    List<ShippingDo> getShippingsByUserId(Integer userId);
}
