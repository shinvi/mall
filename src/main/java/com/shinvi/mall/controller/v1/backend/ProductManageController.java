package com.shinvi.mall.controller.v1.backend;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.base.aop.annotation.ValidAdmin;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.pojo.domain.ProductDo;
import com.shinvi.mall.pojo.vo.ServerResponse;
import com.shinvi.mall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 邱长海
 */
@RestController
@RequestMapping(value = "manage/product", headers = "version=1.0")
public class ProductManageController {

    @Autowired
    private IProductService productService;

    @ValidAdmin
    @RequestMapping(method = RequestMethod.POST)
    public ServerResponse<ProductDo> addProduct(ProductDo product) {
        if (product.getCategoryId() == null) {
            return ServerResponse.error("商品类别不能为空");
        }
        if (StringUtils.isBlank(product.getName())) {
            return ServerResponse.error("商品名称不能为空");
        }
        return ServerResponse.success(productService.addProduct(product));
    }

    @ValidAdmin
    @RequestMapping(method = RequestMethod.PUT)
    public ServerResponse<ProductDo> updateProduct(ProductDo product) {
        if (product.getId() == null) {
            return ServerResponse.error("商品id不能为空");
        }
        return ServerResponse.success(productService.updateProduct(product));
    }

    @ValidAdmin
    @RequestMapping(value = "/{" + Const.ID + "}", method = RequestMethod.GET)
    public ServerResponse<ProductDo> getProductById(@PathVariable(Const.ID) Integer id) {
        return ServerResponse.success(productService.getProductById(id));
    }

    @ValidAdmin
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo<ProductDo>> getProducts(@RequestParam(defaultValue = "1", required = false) int page,
                                                           @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                           String name) {
        return ServerResponse.success(productService.getProducts(page, pageSize, name));
    }

}
