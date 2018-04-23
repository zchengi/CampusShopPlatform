package com.cheng.o2o.service;

import com.cheng.o2o.dto.UserAwardMapExecution;
import com.cheng.o2o.dto.UserShopMapExecution;
import com.cheng.o2o.entity.UserAwardMap;
import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.exceptions.UserAwardMapOperationException;

/**
 * @author cheng
 *         2018/4/23 13:23
 */
public interface UserAwardMapService {

    /**
     * 领取奖品，添加映射信息
     *
     * @param userAwardMap
     * @return
     * @throws UserAwardMapOperationException
     */
    UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;

    /**
     * 修改映射信息，这里主要修改奖品领取状态
     *
     * @param userAwardMap
     * @return
     * @throws UserAwardMapOperationException
     */
    UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException;


    /**
     * 根据传入的查询信息分页查询映射列表及总数
     *
     * @param userAwardMapCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition, Integer pageIndex, Integer pageSize);

    /**
     * 根据传入的id获取映射信息
     *
     * @param userAwardMapId
     * @return
     */
    UserAwardMap getUserAwardMapById(long userAwardMapId);
}
