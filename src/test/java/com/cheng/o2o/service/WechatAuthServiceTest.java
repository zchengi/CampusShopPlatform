package com.cheng.o2o.service;

import com.cheng.o2o.dto.WechatAuthExecution;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.WeChatAuth;
import com.cheng.o2o.enums.WechatAuthStateEnum;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * WechatAuthService Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>04/14/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatAuthServiceTest  {

    @Autowired
    private WechatAuthService wechatAuthService;

    @Test
    public void testBGetWechatAuthByOpenId() {

        WeChatAuth weChatAuth = wechatAuthService.getWechatAuthByOpenId("qwer");
        System.out.println(weChatAuth.getPersonInfo().getName());
    }

    @Test
    public void testARegister() {

        // 新增一条微信帐号
        WeChatAuth weChatAuth = new WeChatAuth();
        PersonInfo personInfo = new PersonInfo();

        String openId = "qwer ";
        // 给微信帐号设置上用户信息，但不设置用户id
        // 希望创建微信帐号的时候自动创建用户信息
        personInfo.setCreateTime(new Date());
        personInfo.setName("test");
        personInfo.setUserType(1);
        weChatAuth.setOpenId(openId);
        weChatAuth.setCreateTime(new Date());
        weChatAuth.setPersonInfo(personInfo);

        WechatAuthExecution wae = wechatAuthService.register(weChatAuth);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wae.getState());
    }
}