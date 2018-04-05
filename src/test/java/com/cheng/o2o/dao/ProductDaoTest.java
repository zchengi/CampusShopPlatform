package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.entity.ProductCategory;
import com.cheng.o2o.entity.Shop;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


/**
 * ProductDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/05/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testAInsertProduct() {
        Shop shop1 = new Shop();
        shop1.setShopId(1L);
        ProductCategory pc1 = new ProductCategory();
        pc1.setProductCategoryId(2L);

        // 初始化三个商品实例并添加进 shopId 为 2 的店铺里，同时商品类别id也为2
        Product product1 = new Product();
        product1.setProductName("测试1");
        product1.setProductDesc("测试描述1");
        product1.setImgAddr("test1");
        product1.setPriority(1);
        product1.setEnableStatus(1);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop1);
        product1.setProductCategory(pc1);

        Product product2 = new Product();
        product2.setProductName("测试2");
        product2.setProductDesc("测试描述2");
        product2.setImgAddr("test2");
        product2.setPriority(2);
        product2.setEnableStatus(0);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop1);
        product2.setProductCategory(pc1);

        Product product3 = new Product();
        product3.setProductName("test3");
        product3.setProductDesc("测试描述3");
        product3.setImgAddr("test3");
        product3.setPriority(3);
        product3.setEnableStatus(1);
        product3.setCreateTime(new Date());
        product3.setLastEditTime(new Date());
        product3.setShop(shop1);
        product3.setProductCategory(pc1);

        // 判断依次添加是否成功
        int effectedNum = productDao.insertProduct(product1);
        Assert.assertEquals(1, effectedNum);
        effectedNum = productDao.insertProduct(product2);
        Assert.assertEquals(1, effectedNum);
        effectedNum = productDao.insertProduct(product3);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    public void testUpdateProduct() {
    }

    @Test
    public void tesBtQueryProductList() {
    }

    @Test
    public void testQueryProductCount() {
    }
}