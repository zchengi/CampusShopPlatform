package com.cheng.o2o.service;

import com.cheng.o2o.dto.UserProductMapExecution;
import com.cheng.o2o.entity.UserProductMap;
import com.cheng.o2o.exceptions.UserProductMapOperationException;

/**
 * @author cheng
 *         2018/4/22 14:51
 */
public interface UserProductMapService {

    /**
     * 添加用户消费记录
     *
     * @param userProductMap
     * @return
     * @throws UserProductMapOperationException
     */
    UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException;

    /**
     * 通过传入的查询条件分页列出用户消费信息列表
     *
     * @param userProductMapCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition,
                                               Integer pageIndex, Integer pageSize);
}
