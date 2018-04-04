package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ProductCategoryDao Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>04/4/2018</pre>
 */
public class ProductCategoryDaoTest extends BaseTest {

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testBatchInsertProductCategory() {
        List<ProductCategory> productCategoryList = new ArrayList<>();
        ProductCategory productCategory1 = new ProductCategory();
        ProductCategory productCategory2 = new ProductCategory();

        productCategory1.setProductCategoryName("商品类别4");
        productCategory1.setPriority(4);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);

        productCategory2.setProductCategoryName("商品类别5");
        productCategory2.setPriority(5);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(1L);

        productCategoryList.add(productCategory1);
        productCategoryList.add(productCategory2);

        int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
        Assert.assertEquals(2, effectedNum);
    }

    @Test
    @Ignore
    public void queryProductCategoryList() {
        long shopId = 1;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println("该店铺自定义类别数为: " + productCategoryList.size());
    }
}