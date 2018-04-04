package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/3/31 12:27
 */
public interface ShopCategoryDao {

    /**
     * 查询店铺类别
     *
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
