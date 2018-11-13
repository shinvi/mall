package com.shinvi.mall.service;


import com.github.pagehelper.PageInfo;
import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.vo.CartProductVo;

import java.util.List;

/**
 * @author 邱长海
 */
public interface ICartService {

    CartProductVo addCart(Integer userId, Integer count, Integer productId);

    CartProductVo reduceCart(Integer userId, Integer count, Integer productId);

    PageInfo<CartProductVo> getCartsByUserId(int page, int pageSize, Integer userId);

    List<CartDo> deleteCart(Integer userId, Integer... ids);
}
