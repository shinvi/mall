package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.ProductDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductDo record);

    int insertSelective(ProductDo record);

    ProductDo selectByPrimaryKey(Integer id);

    ProductDo selectOnlineByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductDo record);

    int updateByPrimaryKey(ProductDo record);

    int countPrimaryKey(Integer id);

    List<ProductDo> selectAllByName(@Param("name") String name);

    List<ProductDo> selectOnlineAllByNameNCategoryIds(@Param("name") String name, @Param("categoryIds") List<Integer> categoryIds);

}