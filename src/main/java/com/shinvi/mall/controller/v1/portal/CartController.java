package com.shinvi.mall.controller.v1.portal;

import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "cart", headers = "version=1.0")
public class CartController {

    @Autowired
    private ICartService cartService;

    @ValidToken
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ServerResponse<CartDo> addCart(@RequestAttribute(Const.User.USER_ID) Integer userId, Integer count, Integer productId) {
        if (count == null) {
            return ServerResponse.error("商品数量不能为空");
        }
        if (productId == null) {
            return ServerResponse.error("商品id不能为空");
        }
        return ServerResponse.success(cartService.addCart(userId, count, productId));
    }
}
