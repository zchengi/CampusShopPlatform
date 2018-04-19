package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.LocalAuthDao;
import com.cheng.o2o.dto.LocalAuthExecution;
import com.cheng.o2o.entity.LocalAuth;
import com.cheng.o2o.enums.LocalAuthStateEnum;
import com.cheng.o2o.exceptions.LocalAuthOperationException;
import com.cheng.o2o.service.LocalAuthService;
import com.cheng.o2o.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cheng
 *         2018/4/17 12:16
 */
@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {

        // 空值判断，传入的 localAuth 帐号密码，用户信息的userId 不能为空，否则返回错误信息
        if (localAuth == null || localAuth.getUsername() == null || localAuth.getPassword() == null
                || localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }

        // 查询此用户是否已绑定过平台帐号
        LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
        if (tempAuth == null) {
            try {
                // 创建平台帐号并绑定
                localAuth.setCreateTime(new Date());
                localAuth.setLastEditTime(new Date());
                // 密码进行 MD5加密
                localAuth.setPassword(MD5Util.getMd5(localAuth.getPassword()));
                int effectedNum = localAuthDao.insertLocalAuth(localAuth);

                // 判断是否创建成功
                if (effectedNum <= 0) {
                    throw new LocalAuthOperationException("帐号绑定失败");
                } else {
                    return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
                }
            } catch (Exception e) {
                throw new LocalAuthOperationException("insertLocalAuth error : " + e.getMessage());
            }
        } else {
            // 如果之前绑定过直接返回错误信息
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
            throws LocalAuthOperationException {

        // 非空判断，判断传入的用户Id,帐号,新旧密码是否为空，新旧密码是否相同，若不满足条件则返回错误信息
        if (userId != null && username != null && password != null && newPassword != null && !password.equals(newPassword)) {
            try {
                int effectedNum = localAuthDao.updateLocalAuth(userId, username,
                        MD5Util.getMd5(password), MD5Util.getMd5(newPassword), new Date());

                if (effectedNum <= 0) {
                    throw new LocalAuthOperationException("更新密码失败");
                }

                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new LocalAuthOperationException("更新密码失败 : " + e.toString());
            }

        } else {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }

    }

    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String username, String password) {
        return localAuthDao.queryLocalByUsernameAndPwd(username, MD5Util.getMd5(password));
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }
}
