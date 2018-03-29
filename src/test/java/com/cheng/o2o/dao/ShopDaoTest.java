package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.Area;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.ShopCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * ShopDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>03/29/2018</pre>
 */
public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;

    @Test
    @Ignore
    public void insertShop() {

        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2L);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺1");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");

        int effectedNum = shopDao.insertShop(shop);
        Assert.assertEquals(1, effectedNum);
    }

    @Test
    public void updateShop() {

        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述3");
        shop.setShopAddr("测试地址3");
        shop.setAdvice("审核中2");
        shop.setLastEditTime(new Date());

        int effectedNum = shopDao.updateShop(shop);
        Assert.assertEquals(1, effectedNum);
    }
}