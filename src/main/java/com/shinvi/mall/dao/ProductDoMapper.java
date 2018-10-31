package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.ProductDo;

public interface ProductDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductDo record);

    int insertSelective(ProductDo record);

    ProductDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductDo record);

    int updateByPrimaryKey(ProductDo record);
}