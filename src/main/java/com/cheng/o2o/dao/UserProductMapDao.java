package com.cheng.o2o.dao;

import com.cheng.o2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/4/20 13:58
 */
public interface UserProductMapDao {

    /**
     * 添加一条用户购买商品的记录
     *
     * @param userProductMap
     * @return
     */
    int insertProductMap(UserProductMap userProductMap);

    /**
     * 根据查询条件分页返回用户购买商品的记录列表
     *
     * @param userProductMapCondition
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<UserProductMap> queryUserProductMapList(@Param("userProductMapCondition") UserProductMap userProductMapCondition,
                                                 @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合 queryUserProductMapList 根据相同的查询条件返回用户购买商品的记录总数
     *
     * @param userProductMapCondition
     * @return
     */
    int queryUserProductMapCount(@Param("userProductMapCondition") UserProductMap userProductMapCondition);
}
