package com.cheng.o2o.service;

import com.cheng.o2o.entity.ShopCategory;

import java.util.List;

/**
 * @author cheng
 *         2018/3/31 13:03
 */
public interface ShopCategoryService {

    /**
     * 当前类在 redis 中存储的 key
     */
    String SHOP_CATEGORY_LIST_KEY = "shopCategoryList";

    /**
     * 根据查询条件获取 shopCategory 列表
     *
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
