package com.shinvi.mall.pojo.vo;

import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.domain.ProductDo;
import org.springframework.beans.BeanUtils;


/**
 * @author 邱长海
 */
public class CartProductVo extends CartDo {
    private ProductDo product;

    public CartProductVo(CartDo cart) {
        BeanUtils.copyProperties(cart, this);
    }

    public ProductDo getProduct() {
        return product;
    }

    public void setProduct(ProductDo product) {
        this.product = product;
    }
}
