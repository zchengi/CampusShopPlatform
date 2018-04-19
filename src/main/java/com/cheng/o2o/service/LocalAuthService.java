package com.cheng.o2o.service;

import com.cheng.o2o.dto.LocalAuthExecution;
import com.cheng.o2o.entity.LocalAuth;
import com.cheng.o2o.exceptions.LocalAuthOperationException;

/**
 * @author cheng
 *         2018/4/17 11:54
 */
public interface LocalAuthService {

    /**
     * 绑定微信，生成平台专属的帐号
     *
     * @param localAuth
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

    /**
     * 修改平台帐号的登录密码
     *
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @return
     * @throws LocalAuthOperationException
     */
    LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
    throws LocalAuthOperationException;

    /**
     * 通过帐号和密码获取平台帐号信息
     *
     * @param username
     * @param password
     * @return
     */
    LocalAuth getLocalAuthByUsernameAndPwd(String username, String password);

    /**
     * 通过 userId 获取平台帐号信息
     *
     * @param userId
     * @return
     */
    LocalAuth getLocalAuthByUserId(long userId);
}
