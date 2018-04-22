package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.ProductSellDaily;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * ProductSellDailyDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/22/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest extends BaseTest {

    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void testAInsertProductSellDaily() {
        // 创建商品日销量统计
        int effectedNum = productSellDailyDao.insertProductSellDaily();
        assertEquals(3, effectedNum);
    }

    @Test
    public void tesBInsertDefaultProductSellDaily() {
        // 创建商品日销量统计
        int effectedNum = productSellDailyDao.insertDefaultProductSellDaily();
        assertEquals(1, effectedNum);
    }

    @Test
    public void testCQueryProductSellDailyList() {
        List<ProductSellDaily> productSellDailyList = productSellDailyDao.
                queryProductSellDailyList(new ProductSellDaily(), null, null);

        System.out.println(productSellDailyList.size());
    }
}