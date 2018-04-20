package com.cheng.o2o.dao;

import com.cheng.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/4/20 15:16
 */
public interface UserShopMapDao {

    /**
     * 添加一条用户店铺积分信息
     *
     * @param userShopMap
     * @return
     */
    int insertUserShopMap(UserShopMap userShopMap);

    /**
     * 更新用户在某店铺的积分
     *
     * @param userShopMap
     * @return
     */
    int updateUserShopMapPoint(UserShopMap userShopMap);

    /**
     * 根据查询条件分页返回用户店铺积分列表
     *
     * @param userShopMapCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserShopMap> queryUserShopMapList(@Param("userShopMapCondition") UserShopMap userShopMapCondition,
                                           @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合 queryUserShopMapList 根据相同的查询条件返回用户店铺积分记录总数
     *
     * @param userShopMapCondition
     * @return
     */
    int queryUserShopMapCount(@Param("userShopMapCondition") UserShopMap userShopMapCondition);

    /**
     * 根据传入的用户id和shopId查询该用户在某个店铺的积分信息
     *
     * @param userId
     * @param shopId
     * @return
     */
    UserShopMap queryUserShopMap(@Param("userId") long userId, @Param("shopId") long shopId);
}
