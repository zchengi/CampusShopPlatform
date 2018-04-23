package com.cheng.o2o.service;

import com.cheng.o2o.dto.UserShopMapExecution;
import com.cheng.o2o.entity.UserShopMap;

/**
 * @author cheng
 *         2018/4/23 12:16
 */
public interface UserShopMapService {

    /**
     * 根据传入的查询信息分页查询用户积分列表
     *
     * @param userShopMapCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserShopMapExecution listUserShopMap(UserShopMap userShopMapCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据用户id和店铺id返回该用户在某个店铺的积分信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap getUserShopMap(long userId, long shopId);
}
