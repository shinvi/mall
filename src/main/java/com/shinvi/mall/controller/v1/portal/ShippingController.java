package com.shinvi.mall.controller.v1.portal;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.ShippingDo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IShippingService;
import com.shinvi.mall.util.ObjectUtils;
import com.shinvi.mall.util.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "shipping", headers = "version=1.0")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @ValidToken
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<List<ShippingDo>> getShippings(@RequestAttribute(Const.User.USER_ID) Integer userId) {
        return ServerResponse.success(shippingService.getShippingsByUserId(userId));
    }

    @ValidToken
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<ShippingDo> addShipping(@RequestAttribute(Const.User.USER_ID) Integer userId, ShippingDo shipping) {
        if (StringUtils.isBlank(shipping.getReceiverName())) {
            return ServerResponse.error("收货人姓名不能为空");
        }
        if (StringUtils.isBlank(shipping.getReceiverMobile())) {
            return ServerResponse.error("收货人手机号不能为空");
        } else if (!PatternUtils.checkMobile(shipping.getReceiverMobile())) {
            return ServerResponse.error("收货人手机号格式错误");
        }
        if (StringUtils.isBlank(shipping.getReceiverProvince())) {
            return ServerResponse.error("省份不能为空");
        }
        if (StringUtils.isBlank(shipping.getReceiverCity())) {
            return ServerResponse.error("城市不能为空");
        }
        if (StringUtils.isBlank(shipping.getReceiverDistrict())) {
            return ServerResponse.error("县/区不能为空");
        }
        if (StringUtils.isBlank(shipping.getReceiverAddress())) {
            return ServerResponse.error("详细地址不能为空");
        }
        if (StringUtils.isNotBlank(shipping.getReceiverZip()) && !PatternUtils.checkPostcode(shipping.getReceiverZip())) {
            return ServerResponse.error("邮政编码格式错误");
        }
        return ServerResponse.success(shippingService.addShipping(userId, shipping));
    }

    @ValidToken
    @RequestMapping(value = "/{" + Const.ID + "}", method = RequestMethod.DELETE)
    public ServerResponse<ShippingDo> deleteShipping(@RequestAttribute(Const.User.USER_ID) Integer userId, @PathVariable(Const.ID) Integer id) {
        return ServerResponse.success(ObjectUtils.with(new ShippingDo(), o -> o.setId(shippingService.deleteShippingByIdNUserId(userId, id))));
    }

    @ValidToken
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse<ShippingDo> updateShipping(@RequestAttribute(Const.User.USER_ID) Integer userId, ShippingDo shipping) {
        if (shipping.getReceiverName() != null && StringUtils.isBlank(shipping.getReceiverName())) {
            return ServerResponse.error("收货人姓名不能为空");
        }
        if (shipping.getReceiverMobile() != null && !PatternUtils.checkMobile(shipping.getReceiverMobile())) {
            return ServerResponse.error("收货人手机号格式错误");
        }
        if (shipping.getReceiverProvince() != null && StringUtils.isBlank(shipping.getReceiverProvince())) {
            return ServerResponse.error("省份不能为空");
        }
        if (shipping.getReceiverCity() != null && StringUtils.isBlank(shipping.getReceiverCity())) {
            return ServerResponse.error("城市不能为空");
        }
        if (shipping.getReceiverDistrict() != null && StringUtils.isBlank(shipping.getReceiverDistrict())) {
            return ServerResponse.error("县/区不能为空");
        }
        if (shipping.getReceiverAddress() != null & StringUtils.isBlank(shipping.getReceiverAddress())) {
            return ServerResponse.error("详细地址不能为空");
        }
        if (shipping.getReceiverZip() != null && !PatternUtils.checkPostcode(shipping.getReceiverZip())) {
            return ServerResponse.error("邮政编码格式错误");
        }
        return ServerResponse.success(shippingService.updateShipping(userId, shipping));
    }
}
