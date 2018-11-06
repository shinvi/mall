package com.shinvi.mall.controller.v1.backend;

import com.shinvi.mall.base.aop.annotation.ValidAdmin;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.CategoryDo;
import com.shinvi.mall.pojo.vo.CategoryVo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/manage/category", headers = "version=1.0")
public class CategoryManageController {

    @Autowired
    private ICategoryService categoryService;

    @ValidAdmin
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<CategoryDo> addCategory(CategoryDo category) {
        if (StringUtils.isBlank(category.getName())) {
            return ServerResponse.error("类别名称不能为空");
        }
        return ServerResponse.success(categoryService.addCategory(category));
    }

    @ValidAdmin
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse<CategoryDo> updateCategory(CategoryDo category) {
        if (category.getId() == null) {
            return ServerResponse.error("修改失败,没有此类别");
        }
        return ServerResponse.success(categoryService.updateCategory(category));
    }

    @ValidAdmin
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<List<CategoryDo>> getCategoriesByParentId(@RequestParam(defaultValue = "0", required = false) Integer parentId) {
        return ServerResponse.success(categoryService.getCategoriesByParentId(parentId));
    }

    @ValidAdmin
    @RequestMapping(value = "/{" + Const.ID + "}", method = RequestMethod.GET)
    public ServerResponse<CategoryVo> getCategoryById(@PathVariable(Const.ID) Integer id) {
        return ServerResponse.success(categoryService.getCategoryWithChildrenById(id));
    }
}
