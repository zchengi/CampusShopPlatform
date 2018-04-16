package com.cheng.o2o.service;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * ShopCategoryService Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/16/2018</pre>
 */
public class ShopCategoryServiceTest extends BaseTest {

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Test
    public void testGetShopCategoryList() {

        List<ShopCategory> categoryList = shopCategoryService.getShopCategoryList(null);
        System.out.println(categoryList.get(0).getShopCategoryName());

        ShopCategory shopCategoryCondition = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(3L);
        shopCategoryCondition.setParent(parent);

        categoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
        System.out.println(categoryList.get(0).getShopCategoryName());
    }
}