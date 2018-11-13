package com.shinvi.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.dao.ShippingDoMapper;
import com.shinvi.mall.pojo.domain.ShippingDo;
import com.shinvi.mall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 邱长海
 */
@Service
public class ShippingService implements IShippingService {

    @Autowired
    private ShippingDoMapper shippingDoMapper;

    @Transactional
    @Override
    public ShippingDo addShipping(Integer userId, ShippingDo shipping) {
        shipping.setUserId(userId);
        shipping.setId(null);
        if (shippingDoMapper.insert(shipping) <= 0) {
            throw new ServerResponseException("新增收货地址失败");
        }
        return shipping;
    }

    @Transactional
    @Override
    public int deleteShippingByIdNUserId(Integer userId, Integer id) {
        if (shippingDoMapper.deleteByUserIdNPrimaryKey(userId, id) <= 0) {
            throw new ServerResponseException("删除收货地址失败");
        }
        return id;
    }

    @Transactional
    @Override
    public ShippingDo updateShipping(Integer userId, ShippingDo shipping) {
        shipping.setUserId(userId);
        if (shippingDoMapper.updateByUserIdNPrimaryKeySelective(shipping) <= 0) {
            throw new ServerResponseException("修改收货地址失败");
        }
        return shippingDoMapper.selectByPrimaryKey(shipping.getId());
    }

    @Override
    public List<ShippingDo> getShippingsByUserId(Integer userId) {
        return shippingDoMapper.selectByUserId(userId);
    }
}
