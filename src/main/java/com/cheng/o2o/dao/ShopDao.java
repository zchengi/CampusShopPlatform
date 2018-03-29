package com.cheng.o2o.dao;

import com.cheng.o2o.entity.Shop;

/**
 * @author cheng
 *         2018/3/29 10:08
 */
public interface ShopDao {

    /**
     * 新增店铺
     *
     * @param shop
     * @return effectedNum
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     *
     * @param shop
     * @return effectedNum
     */
    int updateShop(Shop shop);
}
