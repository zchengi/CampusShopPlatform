package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.LocalAuth;
import com.cheng.o2o.entity.PersonInfo;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * LocalAuthDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/17/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthDaoTest extends BaseTest {

    @Autowired
    private LocalAuthDao localAuthDao;
    private static final String username = "cheng";
    private static final String password = "zzz";

    @Test
    public void testAInsertLocalAuth() {
        // 新增一条平台帐号信息
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);

        // 给平台帐号绑定用户信息
        localAuth.setPersonInfo(personInfo);
        // 设置用户名和密码
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());

        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1, effectedNum);
    }

    @Test
    public void testDUpdateLocalAuth() {
        // 根据用户id，平台帐号，以及旧密码修改登录密码
        Date now = new Date();
        int effectedNum = localAuthDao.updateLocalAuth(1L, username, password, "new", now);
        assertEquals(1, effectedNum);
        // 查询出该平台帐号的最新信息
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        // 输出新密码
        System.out.println(localAuth.getPassword());
    }

    @Test
    public void testBQueryLocalByUserNameAndPwd() {
        // 按照帐号密码查询用户信息
        LocalAuth localAuth = localAuthDao.queryLocalByUsernameAndPwd(username, password);
        assertEquals("cheng", localAuth.getPersonInfo().getName());
    }

    @Test
    public void testCQueryLocalByUserId() {
        // 按照用户id查询平台帐号，进而获取用户信息
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        assertEquals("cheng", localAuth.getPersonInfo().getName());
    }
}