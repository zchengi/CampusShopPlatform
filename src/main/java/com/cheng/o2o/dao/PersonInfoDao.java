package com.cheng.o2o.dao;

import com.cheng.o2o.entity.PersonInfo;

/**
 * @author cheng
 *         2018/4/14 15:12
 */
public interface PersonInfoDao {

    /**
     * 通过用户id查询用户
     *
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * 添加用户信息
     *
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);
}
