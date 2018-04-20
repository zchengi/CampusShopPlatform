package com.cheng.o2o.dao;

import com.cheng.o2o.entity.ShopAuthMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/4/20 16:01
 */
public interface ShopAuthMapDao {

    /**
     * 新增一条店铺与店员的授权信息
     *
     * @param shopAuthMap
     * @return
     */
    int insertShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 更改授权信息
     *
     * @param shopAuthMap
     * @return
     */
    int updateShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 对某员工除权
     *
     * @param shopAuthId
     * @return
     */
    int deleteShopAuthMap(long shopAuthId);

    /**
     * 分页列出店铺下面的授权信息
     *
     * @param shopId
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<ShopAuthMap> queryShopAuthMapListByShopId(@Param("shopId") long shopId,
                                                   @Param("rowIndex") int rowIndex,
                                                   @Param("pageSize") int pageSize);

    /**
     * 获取授权总数
     *
     * @param shopId
     * @return
     */
    int queryShopAuthCountByShopId(@Param("shopId") long shopId);

    /**
     * 通过 shopAuthId 查询员工授权信息
     *
     * @param shopAuthId
     * @return
     */
    ShopAuthMap queryShopAuthMapById(long shopAuthId);
}
