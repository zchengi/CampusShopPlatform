package com.cheng.o2o.service;

import com.cheng.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author cheng
 *         2018/3/31 13:03
 */
public interface ShopCategoryService {

    /**
     * 获取商铺列表
     *
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
