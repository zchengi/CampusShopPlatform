package com.cheng.o2o.service;

import com.cheng.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/22 13:53
 */
public interface ProductSellDailyService {

    /**
     * 每日定时对所有店铺的商品销量进行统计
     */
    void dailyCalculate();

    /**
     * 根据查询条件返回日销售的统计列表
     *
     * @param productSellDailyCondition
     * @param beginTime
     * @param endTime
     * @return
     */
    List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition,
                                                Date beginTime, Date endTime);
}
