package com.shinvi.mall.dao;

import com.shinvi.mall.pojo.domain.CategoryDo;

public interface CategoryDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CategoryDo record);

    int insertSelective(CategoryDo record);

    CategoryDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CategoryDo record);

    int updateByPrimaryKey(CategoryDo record);
}