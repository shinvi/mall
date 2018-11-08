package com.shinvi.mall.controller.v1.portal;

import com.github.pagehelper.PageInfo;
import com.shinvi.mall.common.Const;
import com.shinvi.mall.common.ResponseCode;
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
@RequestMapping(value = "product", headers = "version=1.0")
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping(value = "/{" + Const.ID + "}", method = RequestMethod.GET)
    public ServerResponse<ProductDo> getProductById(@PathVariable(Const.ID) Integer id) {
        return ServerResponse.success(productService.getOnlineProductById(id));
    }

    /**
     * @param keyword 搜索条件
     */
    @RequestMapping(method = RequestMethod.GET)
    public ServerResponse<PageInfo<ProductDo>> getProducts(@RequestParam(defaultValue = "1", required = false) int page,
                                                           @RequestParam(defaultValue = "10", required = false) int pageSize,
                                                           String keyword, Integer categoryId, String order) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.error(ResponseCode.ILLEGAL_ARGUMENT);
        }
        return ServerResponse.success(productService.getOnlineProducts(page, pageSize, keyword, categoryId, order));
    }
}
