package com.shinvi.mall.pojo.vo;

import com.shinvi.mall.pojo.domain.CategoryDo;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class CategoryVo extends CategoryDo {

    private List<CategoryVo> children;

    public CategoryVo() {
    }

    public CategoryVo(CategoryDo category) {
        BeanUtils.copyProperties(category, this);
    }

    public List<CategoryVo> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryVo> children) {
        this.children = children;
    }
}
