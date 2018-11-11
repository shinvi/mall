package com.shinvi.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.CartDoMapper;
import com.shinvi.mall.dao.ProductDoMapper;
import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.domain.ProductDo;
import com.shinvi.mall.pojo.vo.CartProductVo;
import com.shinvi.mall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 邱长海
 */
@Service
public class CartService implements ICartService {

    @Autowired
    private CartDoMapper cartDoMapper;

    @Autowired
    private ProductDoMapper productDoMapper;

    @Transactional
    @Override
    public CartProductVo addCart(Integer userId, Integer count, Integer productId) {
        ProductDo product = productDoMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus() != Const.Product.STATUS_IN_STOCK) {
            throw new ServerResponseException("该商品不存在或已下架");
        }
        if (count > product.getStock()) {
            throw new ServerResponseException("添加购物车失败, 商品库存不足");
        }
        CartDo cart = cartDoMapper.selectByUserIdNProductId(userId, productId);
        if (cart == null) {
            cart = new CartDo();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
        } else {
            cart.setQuantity(count + cart.getQuantity());
        }
        if (cart.getQuantity() > product.getStock()) {
            throw new ServerResponseException("添加购物车失败, 商品库存不足");
        }
        int result;
        if (cart.getId() == null) {
            result = cartDoMapper.insert(cart);
        } else {
            result = cartDoMapper.updateByPrimaryKeySelective(cart);
        }
        if (result <= 0) {
            throw new ServerResponseException("添加购物车失败");
        }
        CartProductVo cartProduct = new CartProductVo(cart);
        cartProduct.setProduct(product);
        return cartProduct;
    }

    @Override
    public PageInfo<CartProductVo> getCartsByUserId(int page, int pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        PageHelper.orderBy(Const.UPDATE_TIME + " " + Const.OrderBy.PRICE_DESC);
        List<CartDo> carts = cartDoMapper.selectALLByUserId(userId);
        List<CartProductVo> cartProducts = Lists.newArrayList();
        for (CartDo cart : carts) {
            ProductDo product = productDoMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null) {
                product = new ProductDo();
                product.setStatus(Const.Product.STATUS_DELETE);
            }
            CartProductVo cartProduct = new CartProductVo(cart);
            cartProduct.setProduct(product);
            cartProducts.add(cartProduct);
        }
        return new PageInfo<>(cartProducts);
    }
}
