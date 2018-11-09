package com.shinvi.mall.pojo.vo;

import com.shinvi.mall.pojo.domain.CartDo;
import com.shinvi.mall.pojo.domain.ProductDo;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @author 邱长海
 */
public class CartProductVo extends CartDo {
    private BigDecimal totalPrice;
    private ProductDo product;

    public CartProductVo(CartDo cart) {
        BeanUtils.copyProperties(cart, this);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductDo getProduct() {
        return product;
    }

    public void setProduct(ProductDo product) {
        this.product = product;
    }
}
