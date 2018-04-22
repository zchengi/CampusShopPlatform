package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ProductSellDaily;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * ProductSellDailyDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/22/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {

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