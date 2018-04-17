package com.cheng.o2o.dao;

import com.cheng.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author cheng
 *         2018/4/17 11:25
 */
public interface LocalAuthDao {

    /**
     * 添加平台帐号
     *
     * @param localAuth
     * @return
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 通过 userId, username, password 修改密码
     *
     * @param userId
     * @param username
     * @param password
     * @param newPassword
     * @param lastEditTime
     * @return
     */
    int updateLocalAuth(@Param("userId") long userId, @Param("username") String username,
                        @Param("password") String password, @Param("newPassword") String newPassword,
                        @Param("lastEditTime") Date lastEditTime);

    /**
     * 通过帐号和密码查询对应的信息(登录)
     *
     * @param username
     * @param password
     * @return
     */
    LocalAuth queryLocalByUsernameAndPwd(@Param("username") String username, @Param("password") String password);

    /**
     * 通过用户id查询对应的 localAuth
     *
     * @param userId
     * @return
     */
    LocalAuth queryLocalByUserId(@Param("userId") long userId);
}
