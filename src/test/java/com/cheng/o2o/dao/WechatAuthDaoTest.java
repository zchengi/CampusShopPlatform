package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.WeChatAuth;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * WechatAuthDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/14/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthDaoTest extends BaseTest {

    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Test
    public void testBQueryWechatInfoByOpenId() {
        WeChatAuth weChatAuth = wechatAuthDao.queryWechatInfoByOpenId("zzz");
        assertEquals("cheng", weChatAuth.getPersonInfo().getName());
    }

    @Test
    public void testAInsertWechatAuth() {

        // 新增一条微信帐号
        WeChatAuth weChatAuth = new WeChatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        // 给微信帐号绑定上用户信息
        weChatAuth.setPersonInfo(personInfo);

        // 随意设定 openId
        weChatAuth.setOpenId("zzz");
        weChatAuth.setCreateTime(new Date());

        int effectedNum = wechatAuthDao.insertWechatAuth(weChatAuth);
        assertEquals(1, effectedNum);
    }
}