package com.cheng.o2o.service;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.dto.ShopExecution;
import com.cheng.o2o.entity.Area;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.ShopCategory;
import com.cheng.o2o.enums.ShopStateEnum;
import com.cheng.o2o.exceptions.ShopOperationException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * ShopService Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>03/29/2018</pre>
 */
public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {

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
        shop.setShopName("测试店铺5");
        shop.setShopDesc("test5");
        shop.setShopAddr("test5");
        shop.setPhone("test5");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("D:/IntelliJProject/O2O/target/test-classes/img/002.png");
        FileInputStream fis = new FileInputStream(shopImg);
        ImageHolder thumbnail = new ImageHolder(shopImg.getName(), fis);
        ShopExecution se = shopService.addShop(shop, thumbnail);
        Assert.assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    @Ignore
    public void testModifyShop() throws ShopOperationException, FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺信息");
        File shopImg = new File("D:/IntelliJProject/O2O/target/test-classes/img/2.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder thumbnail = new ImageHolder("2.jpg", is);
        ShopExecution shopExecution = shopService.modifyShop(shop, thumbnail);
        System.out.println("新的图片地址为: " + shopExecution.getShop().getShopImg());
    }

    @Test
    @Ignore
    public void testGetByShopId() throws ShopOperationException {

        Shop shop = shopService.getByShopId(17L);
        System.out.println("areaId:" + shop.getArea().getAreaId());
        System.out.println("areaName:" + shop.getArea().getAreaName());
    }

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(1L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition, 1, 2);
        System.out.println("店铺列表数为: " + se.getShopList().size());
        System.out.println("店铺总数为: " + se.getCount());
    }
}
