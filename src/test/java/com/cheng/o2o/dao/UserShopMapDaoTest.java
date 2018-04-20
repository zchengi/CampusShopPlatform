package com.cheng.o2o.dao;

import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.UserShopMap;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * UserShopMapDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/20/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserShopMapDaoTest {

    @Autowired
    private UserShopMapDao userShopMapDao;

    @Test
    public void testAInsertUserShopMap() {

        // 创建用户店铺积分统计信息1
        UserShopMap userShopMap = new UserShopMap();
        PersonInfo customer = new PersonInfo();
        customer.setUserId(1L);
        userShopMap.setUser(customer);
        Shop shop = new Shop();
        shop.setShopId(39L);
        userShopMap.setShop(shop);
        userShopMap.setCreateTime(new Date());
        userShopMap.setPoint(1);
        int effectedNum = userShopMapDao.insertUserShopMap(userShopMap);
        assertEquals(1, effectedNum);

        // 创建用户店铺积分统计信息2
        UserShopMap userShopMap2 = new UserShopMap();
        PersonInfo customer2 = new PersonInfo();
        customer2.setUserId(2L);
        userShopMap2.setUser(customer);
        Shop shop2 = new Shop();
        shop2.setShopId(40L);
        userShopMap2.setShop(shop2);
        userShopMap2.setCreateTime(new Date());
        userShopMap2.setPoint(1);
        effectedNum = userShopMapDao.insertUserShopMap(userShopMap2);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testCUpdateUserShopMapPoint() {
        UserShopMap userShopMap = userShopMapDao.queryUserShopMap(1, 39);
        assertTrue("Error，积分不一致!", 1 == userShopMap.getPoint());
        userShopMap.setPoint(2);
        int effectedNUm = userShopMapDao.updateUserShopMapPoint(userShopMap);
        assertEquals(1, effectedNUm);
    }

    @Test
    public void testBQueryUserShopMapList() {

        UserShopMap userShopMap = new UserShopMap();
        // 查所有
        List<UserShopMap> userProductMapList = userShopMapDao.
                queryUserShopMapList(userShopMap, 0, 2);
        assertEquals(2, userProductMapList.size());
        int count = userShopMapDao.queryUserShopMapCount(userShopMap);
        assertEquals(2, count);

        // 按店铺查询
        Shop shop = new Shop();
        shop.setShopId(39L);
        userShopMap.setShop(shop);
        userProductMapList = userShopMapDao.queryUserShopMapList(userShopMap, 0, 2);
        assertEquals(1, userProductMapList.size());
        count = userShopMapDao.queryUserShopMapCount(userShopMap);
        assertEquals(1, count);

        // 按用户id和店铺查询
        userShopMap = userShopMapDao.queryUserShopMap(1, 39);
        assertEquals("测试", userShopMap.getUser().getName());
    }

}