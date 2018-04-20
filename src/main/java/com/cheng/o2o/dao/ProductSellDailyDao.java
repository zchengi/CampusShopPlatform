package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/20 14:39
 */
public interface ProductSellDailyDao {

    /**
     * 统计平台所有商品的日销售量
     *
     * @return
     */
    int insertProductSellDaily();

    /**
     * 根据查询条件返回商品日销售的统计列表
     *
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> queryProductSellDailyList(
            @Param("productSellDailyCondition") ProductSellDaily productSellDailyCondition,
            @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}


