package com.shinvi.mall.controller.v1.portal;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.ValidToken;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.vo.CartProductVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ServerResponse<CartProductVo> addCart(@RequestAttribute(Const.User.USER_ID) Integer userId, Integer count, Integer productId) {
        if (count == null || count <= 0) {
            return ServerResponse.error("商品数量错误");
        }
        if (productId == null) {
            return ServerResponse.error("商品id不能为空");
        }
        return ServerResponse.success(cartService.addCart(userId, count, productId));
    }

    @ValidToken
    @RequestMapping(value = "/product/{" + Const.ID + "}", method = RequestMethod.DELETE)
    public ServerResponse<CartProductVo> reduceCart(@RequestAttribute(Const.User.USER_ID) Integer userId, Integer count,
                                                    @PathVariable(Const.ID) Integer productId) {
        if (count == null || count <= 0) {
            return ServerResponse.error("商品数量错误");
        }
        return ServerResponse.success(cartService.reduceCart(userId, count, productId));
    }

    @ValidToken
    @RequestMapping(method = RequestMethod.DELETE)
    public ServerResponse<List<CartDo>> deleteCart(@RequestAttribute(Const.User.USER_ID) Integer userId, Integer[] id) {
        if (id == null) {
            return ServerResponse.error("购物车id不能为空");
        }
        return ServerResponse.success(cartService.deleteCart(userId, id));
    }

    @ValidToken
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo<CartProductVo>> getCarts(@RequestAttribute(Const.User.USER_ID) Integer userId,
                                                            @RequestParam(defaultValue = "1", required = false) int page,
                                                            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return ServerResponse.success(cartService.getCartsByUserId(page, pageSize, userId));
    }
}
