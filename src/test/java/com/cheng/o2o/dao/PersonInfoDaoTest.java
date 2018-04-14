package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.PersonInfo;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * PersonInfoDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/14/2018</pre>
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonInfoDaoTest extends BaseTest {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testBQueryPersonInfoById() {

        // 查询id为1的用户信息
        long userId = 1;
        PersonInfo person = personInfoDao.queryPersonInfoById(userId);
        System.out.println(person.getName());
    }

    @Test
    public void testAInsertPersonInfo() {

        // 设置新增的用户信息
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("cheng");
        personInfo.setGender("zz");
        personInfo.setUserType(1);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        personInfo.setEnableStatus(1);
        int effectedNum = personInfoDao.insertPersonInfo(personInfo);
        assertEquals(1, effectedNum);
    }
}