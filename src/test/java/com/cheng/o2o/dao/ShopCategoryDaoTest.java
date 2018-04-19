package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * ShopCategoryDao Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>03/31/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest  {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory() {
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);
        System.out.println(shopCategoryList.size());

    }
}