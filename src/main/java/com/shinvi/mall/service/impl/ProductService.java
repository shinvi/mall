package com.shinvi.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.shinvi.mall.base.aop.annotation.Transactional;
import com.shinvi.mall.base.exception.ServerResponseException;
import com.shinvi.mall.base.service.BaseService;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.dao.CategoryDoMapper;
import com.shinvi.mall.dao.ProductDoMapper;
import com.shinvi.mall.pojo.domain.ProductDo;
import com.shinvi.mall.pojo.vo.CategoryVo;
import com.shinvi.mall.service.ICategoryService;
import com.shinvi.mall.service.IProductService;
import com.shinvi.mall.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 邱长海
 */
@Service
public class ProductService extends BaseService implements IProductService {

    @Autowired
    private ProductDoMapper productDoMapper;

    @Autowired
    private CategoryDoMapper categoryDoMapper;

    @Autowired
    private ICategoryService categoryService;

    @Transactional
    @Override
    public ProductDo addProduct(ProductDo product) {
        validPropertyExist(Const.Product.CATEGORY_ID, String.valueOf(product.getCategoryId()), false);

        handleProductImage(product);

        if (product.getPrice() == null) {
            throw new ServerResponseException("商品价格不能为空");
        }
        if (product.getStock() == null) {
            throw new ServerResponseException("商品数量不能为空");
        }
        if (!ObjectUtils.in(product.getStatus(), Const.Product.STATUS_IN_STOCK, Const.Product.STATUS_OUT_STOCK)) {
            throw new ServerResponseException("商品状态错误");
        }

        if (productDoMapper.insert(product) <= 0) {
            throw new ServerResponseException("添加商品失败");
        }
        return product;
    }

    @Transactional
    @Override
    public ProductDo updateProduct(ProductDo product) {
        validPropertyExist(Const.ID, String.valueOf(product.getId()), false);
        handleProductImage(product);

        if (product.getStatus() != null &&
                !ObjectUtils.in(product.getStatus(), Const.Product.STATUS_IN_STOCK, Const.Product.STATUS_OUT_STOCK, Const.Product.STATUS_DELETE)) {
            throw new ServerResponseException("商品状态错误");
        }
        if (productDoMapper.updateByPrimaryKeySelective(product) <= 0) {
            throw new ServerResponseException("修改商品信息失败");
        }
        product = productDoMapper.selectByPrimaryKey(product.getId());
        return product;
    }

    @Override
    public ProductDo getProductById(Integer id) {
        ProductDo product = productDoMapper.selectByPrimaryKey(id);
        if (product == null) {
            throw new ServerResponseException("该商品不存在或已删除");
        }
        return product;
    }

    @Override
    public ProductDo getOnlineProductById(Integer id) {
        ProductDo product = productDoMapper.selectOnlineByPrimaryKey(id);
        if (product == null) {
            throw new ServerResponseException("该商品不存在或已下架");
        }
        return product;
    }

    @Override
    public PageInfo<ProductDo> getProducts(int page, int pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        if (StringUtils.isNotBlank(name)) {
            name = "%" + name + "%";
        }
        List<ProductDo> products = productDoMapper.selectAllByCondition(name);
        return new PageInfo<>(products);
    }

    @Override
    public PageInfo<ProductDo> getOnlineProducts(int page, int pageSize, String keyword, Integer categoryId, String order) {
        PageHelper.startPage(page, pageSize);
        List<Integer> categoryIds = null;
        if (categoryId != null) {
            CategoryVo category = categoryService.getCategoryWithChildrenById(categoryId);
            categoryIds = gatherCategoryIds(category);
        }
        if (StringUtils.isBlank(keyword)) {
            keyword = null;
        } else {
            keyword = "%" + keyword + "%";
        }

        if (StringUtils.isBlank(order)) {
            order = null;
        } else if (!ObjectUtils.in(order, Const.OrderBy.PRICE_DESC, Const.OrderBy.PRICE_ASC)) {
            throw new ServerResponseException("排序参数非法");
        } else {
            String[] orderBy = order.split("_");
            PageHelper.orderBy(orderBy[0] + " " + orderBy[1]);
        }
        return null;
    }

    @Override
    protected void registerPropertyValidator(Map<String, PropertyValidator> validators) {
        validators.put(Const.Product.CATEGORY_ID, new PropertyValidator(s -> categoryDoMapper.countPrimaryKeyNNotTop(Integer.valueOf(s)),
                "商品类别已存在", "商品类别不存在"));
        validators.put(Const.ID, new PropertyValidator(s -> productDoMapper.countPrimaryKey(Integer.valueOf(s)),
                "商品已存在", "商品不存在"));
    }

    private List<Integer> gatherCategoryIds(CategoryVo category) {
        List<Integer> categoryIds = Lists.newArrayList();
        categoryIds.add(category.getId());
        if (category.getChildren() != null) {
            for (CategoryVo child : category.getChildren()) {
                categoryIds.addAll(gatherCategoryIds(child));
            }
        }
        return categoryIds;
    }

    /**
     * 把商品子图里的第一张设置为主图
     */
    private void handleProductImage(ProductDo product) {
        if (StringUtils.isNoneBlank(product.getSubImages())) {
            String[] imgs = product.getSubImages().split(",");
            if (imgs.length > 0) {
                product.setMainImage(imgs[0]);
            } else {
                product.setMainImage(product.getSubImages());
            }
        }
    }
}
