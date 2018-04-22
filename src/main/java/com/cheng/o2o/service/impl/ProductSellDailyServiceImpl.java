package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.ProductSellDailyDao;
import com.cheng.o2o.entity.ProductSellDaily;
import com.cheng.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/22 13:54
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

    private static Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Override
    public void dailyCalculate() {
        logger.info("Quartz Running! ");
        // 统计在 tb_user_product_map 里面产生销量的每个店铺的各件商品的日销量
        productSellDailyDao.insertProductSellDaily();
        // 统计没有卖出的商品，将其日销量全部置为0 (为了配合echarts使用)
        productSellDailyDao.insertDefaultProductSellDaily();
    }

    @Override
    public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition,
                                                       Date beginTime, Date endTime) {
        return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
    }
}
