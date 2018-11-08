package com.shinvi.mall.service;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.pojo.domain.ProductDo;

/**
 * @author 邱长海
 */
public interface IProductService {

    ProductDo addProduct(ProductDo product);

    ProductDo updateProduct(ProductDo product);

    ProductDo getProductById(Integer id);

    ProductDo getOnlineProductById(Integer id);

    PageInfo<ProductDo> getProducts(int page, int pageSize, String name);

    PageInfo<ProductDo> getOnlineProducts(int page, int pageSize, String keyword, Integer categoryId, String order);
}
