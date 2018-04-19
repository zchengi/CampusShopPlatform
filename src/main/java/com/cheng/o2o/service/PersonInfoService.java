package com.cheng.o2o.service;

import com.cheng.o2o.entity.PersonInfo;

/**
 * @author cheng
 *         2018/4/14 16:18
 */
public interface PersonInfoService {

    /**
     * 根据用户id获取 personInfo 信息
     *
     * @param userId
     * @return
     */
    PersonInfo getPersonInfoById(Long userId);
}
