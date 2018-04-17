package com.cheng.o2o.service;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.dto.LocalAuthExecution;
import com.cheng.o2o.entity.LocalAuth;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.enums.WechatAuthStateEnum;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * LocalAuthService Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/17/2018</pre>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LocalAuthServiceTest extends BaseTest {

    @Autowired
    private LocalAuthService localAuthService;

    private static final String username = "test-username";
    private static final String password = "test-password";

    @Test
    public void testABindLocalAuth() {
        // 新增一条平台帐号
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();

        // 给用户设置上用户Id，标明是某个用户创建的帐号
        personInfo.setUserId(3L);
        // 给平台帐号设置用户信息，标明是与哪个用户绑定
        localAuth.setPersonInfo(personInfo);
        // 设置帐号
        localAuth.setUsername(username);
        // 设置密码
        localAuth.setPassword(password);

        // 绑定帐号
        LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());

        // 通过userId找到新增的 localAuth
        localAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
        // 打印用户昵称和帐号密码
        System.out.println("用户昵称 : " + localAuth.getPersonInfo().getName());
        System.out.println("平台帐号密码 : " + localAuth.getPassword());
    }

    @Test
    public void testBModifyLocalAuth() {

        // 设置帐号 userId 信息
        long userId = 3L;
        String newPassword = "test-new-password";

        // 修改该帐号对应的密码
        LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(userId, username, password, newPassword);
        assertEquals(WechatAuthStateEnum.SUCCESS.getState(), localAuthExecution.getState());

        // 通过帐号密码找到修改后的localAuth
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(username, newPassword);
        // 打印用户昵称和帐号密码
        System.out.println("用户昵称 : " + localAuth.getPersonInfo().getName());
        System.out.println("平台帐号密码 : " + localAuth.getPassword());
    }
}