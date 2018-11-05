package com.shinvi.mall.service.impl;

import com.google.common.collect.Lists;
import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.base.service.BaseService;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.CategoryDoMapper;
import com.shinvi.mall.pojo.domain.CategoryDo;
import com.shinvi.mall.pojo.vo.CategoryVo;
import com.shinvi.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService extends BaseService implements ICategoryService {

    @Autowired
    private CategoryDoMapper categoryDoMapper;

    @Transactional
    @Override
    public CategoryDo addCategory(CategoryDo category) {
        validPropertyExist(Const.Category.NAME, category.getName(), true);
        if (category.getParentId() == null) {
            category.setParentId(0);
        }
        if (category.getParentId() != 0) {
            validPropertyExist(Const.ID, category.getParentId().toString(), false);
        }
        category.setStatus(true);
        if (categoryDoMapper.insert(category) <= 0) {
            throw new ServerResponseException("添加类别失败");
        }
        return category;
    }

    @Transactional
    @Override
    public CategoryDo updateCategory(CategoryDo category) {
        category.setStatus(null);
        category.setParentId(null);
        category.setSortOrder(null);
        if (categoryDoMapper.updateByPrimaryKeySelective(category) <= 0) {
            throw new ServerResponseException("修改失败");
        }
        return categoryDoMapper.selectByPrimaryKey(category.getId());
    }

    @Override
    public List<CategoryDo> getCategoriesByParentId(Integer parentId) {
        return categoryDoMapper.selectByParentId(parentId);
    }

    @Override
    public CategoryVo getCategoryWithChildrenById(Integer id) {
        CategoryDo category = categoryDoMapper.selectByPrimaryKey(id);
        if (category == null) {
            throw new ServerResponseException("无此类别");
        }
        CategoryVo categoryVo = new CategoryVo(category);
        findCategoryChildren(categoryVo);
        return categoryVo;
    }

    private void findCategoryChildren(CategoryVo category) {
        List<CategoryDo> categories = categoryDoMapper.selectByParentId(category.getId());
        List<CategoryVo> children = Lists.newArrayList();
        category.setChildren(children);
        for (CategoryDo cdo : categories) {
            CategoryVo cvo = new CategoryVo(cdo);
            children.add(cvo);
            findCategoryChildren(cvo);
        }
    }

    @Override
    protected void registerPropertyValidator(Map<String, PropertyValidator> validators) {
        validators.put(Const.ID, new PropertyValidator(s -> categoryDoMapper.countPrimaryKey(Integer.valueOf(s)),
                "父类别已存在", "父类别不存在"));
        validators.put(Const.Category.NAME, new PropertyValidator(s -> categoryDoMapper.countName(s),
                "类别名称已存在", "类别名称不存在"));
    }
}
