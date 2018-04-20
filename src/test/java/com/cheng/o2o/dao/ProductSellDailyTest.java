package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ProductSellDaily;
import com.cheng.o2o.entity.Shop;
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
 * @since <pre>04/20/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyTest {

    @Autowired
    ProductSellDailyDao productSellDailyDao;

    @Test
    public void testAInsertProductSellDaily() {

        // 创建商品日销量统计
        int effectedNum = productSellDailyDao.insertProductSellDaily();
        assertEquals(3, effectedNum);
    }

    @Test
    public void testBQueryProductSellDailyList() {

        ProductSellDaily productSellDaily = new ProductSellDaily();
        // 叠加店铺查询
        Shop shop = new Shop();
        shop.setShopId(39L);
        productSellDaily.setShop(shop);

        List<ProductSellDaily> productSellDailyList = productSellDailyDao.
                queryProductSellDailyList(productSellDaily, null, null);
        assertEquals(2, productSellDailyList.size());
    }
}