package com.shinvi.mall.service;


import com.github.pagehelper.PageInfo;
import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.vo.CartProductVo;

/**
 * @author 邱长海
 */
public interface ICartService {

    CartDo addCart(Integer userId, Integer count, Integer productId);

    PageInfo<CartProductVo> getCartsByUserId(int page, int pageSize, Integer userId);
}
