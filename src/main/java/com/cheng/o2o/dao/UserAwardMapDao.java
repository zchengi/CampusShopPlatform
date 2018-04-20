package com.cheng.o2o.dao;

import com.cheng.o2o.entity.UserAwardMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/4/20 12:15
 */
public interface UserAwardMapDao {

    /**
     * 添加一条奖品兑换信息
     *
     * @param userAwardMap
     * @return
     */
    int insertUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 更新奖品兑换信息，主要更新奖品领取状态
     *
     * @param userAwardMap
     * @return
     */
    int updateUserAwardMap(UserAwardMap userAwardMap);

    /**
     * 根据传入的查询条件分页返回用户兑换奖品记录的列表信息
     *
     * @param userAwardMapCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserAwardMap> queryUserAwardMapList(@Param("userAwardMapCondition") UserAwardMap userAwardMapCondition,
                                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合 queryUserAwardMapList 返回相同查询条件下的兑换奖品记录数
     *
     * @param userAwardMapCondition
     * @return
     */
    int queryUserAwardMapCount(@Param("userAwardMapCondition") UserAwardMap userAwardMapCondition);

    /**
     * 根据 userAwardId 返回某条奖品兑换信息
     *
     * @param userAwardId
     * @return
     */
    UserAwardMap queryUserAwardMapById(long userAwardId);
}
