package com.shinvi.mall.service;

import com.shinvi.mall.pojo.domain.CategoryDo;
import com.shinvi.mall.pojo.vo.CategoryVo;

import java.util.List;

public interface ICategoryService {

    CategoryDo addCategory(CategoryDo category);

    CategoryDo updateCategory(CategoryDo category);

    List<CategoryDo> getCategoriesByParentId(Integer parentId);

    CategoryVo getCategoryWithChildrenById(Integer id);
}
