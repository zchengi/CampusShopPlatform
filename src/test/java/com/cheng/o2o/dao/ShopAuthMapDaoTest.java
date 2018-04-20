package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.ShopAuthMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * ShopAuthMapDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/20/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShopAuthMapDaoTest extends BaseTest {

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Test
    public void testAInsertShopAuthMap() {

        // 创建店铺授权信息1
        ShopAuthMap shopAuthMap1 = new ShopAuthMap();
        PersonInfo employee = new PersonInfo();
        employee.setUserId(1L);
        shopAuthMap1.setEmployee(employee);
        Shop shop = new Shop();
        shop.setShopId(1L);
        shopAuthMap1.setShop(shop);
        shopAuthMap1.setTitle("CEO");
        shopAuthMap1.setTitleFlag(1);
        shopAuthMap1.setCreateTime(new Date());
        shopAuthMap1.setLastEditTime(new Date());
        shopAuthMap1.setEnableStatus(1);
        int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap1);
        assertEquals(1, effectedNum);


        // 创建店铺授权信息2
        ShopAuthMap shopAuthMap2 = new ShopAuthMap();
        shopAuthMap2.setEmployee(employee);
        Shop shop2 = new Shop();
        shop2.setShopId(39L);
        shopAuthMap2.setShop(shop2);
        shopAuthMap2.setTitle("打工");
        shopAuthMap2.setTitleFlag(2);
        shopAuthMap2.setCreateTime(new Date());
        shopAuthMap2.setLastEditTime(new Date());
        shopAuthMap2.setEnableStatus(0);
        effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
        assertEquals(1, effectedNum);

    }

    @Test
    public void testCUpdateShopAuthMap() {
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.
                queryShopAuthMapListByShopId(1L, 0, 2);
        shopAuthMapList.get(0).setTitle("zzz");
        shopAuthMapList.get(0).setTitleFlag(2);
        int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMapList.get(0));
        assertEquals(1, effectedNum);
    }

    @Test
    public void testDeleteShopAuthMap() {
        List<ShopAuthMap> shopAuthMapList1 = shopAuthMapDao.
                queryShopAuthMapListByShopId(1, 0, 1);
        List<ShopAuthMap> shopAuthMapList2 = shopAuthMapDao.
                queryShopAuthMapListByShopId(39, 0, 1);

        int effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList1.get(0).getShopAuthId());
        assertEquals(1, effectedNum);
        effectedNum = shopAuthMapDao.deleteShopAuthMap(shopAuthMapList2.get(0).getShopAuthId());
        assertEquals(1, effectedNum);
    }

    @Test
    public void testBQueryShopAuth() {

        // 测试 queryShopAuthMapListByShopId
        List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.
                queryShopAuthMapListByShopId(1L, 0, 2);
        assertEquals(1, shopAuthMapList.size());

        // 测试 queryShopAuthMapById
        ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(shopAuthMapList.get(0).getShopAuthId());
        assertEquals("CEO", shopAuthMap.getTitle());
        System.out.println("employee's name is " + shopAuthMap.getEmployee().getName());
        System.out.println("shop name is " + shopAuthMap.getShop().getShopName());

        // 测试 queryShopAuthCountByShopId
        int count = shopAuthMapDao.queryShopAuthCountByShopId(1);
        assertEquals(1, count);
    }
}