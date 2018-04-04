package com.cheng.o2o.service;

import com.cheng.o2o.entity.ProductCategory;

import java.util.List;

/**
 * @author cheng
 *         2018/4/4 14:30
 */
public interface ProductCategoryService {
    /**
     * 查询指定某个店铺下的所有商品类别信息
     *
     * @param shopId
     * @return
     */
    List<ProductCategory> getProductCategory(long shopId);
}
