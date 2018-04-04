package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ProductCategory;

import java.util.List;

/**
 * @author cheng
 *         2018/4/4 14:10
 */
public interface ProductCategoryDao {

    /**
     * 通过 shop id 查询店铺商品类别
     *
     * @param shopId
     * @return List<ProductCategory>
     */
    List<ProductCategory> queryProductCategoryList(Long shopId);
}
