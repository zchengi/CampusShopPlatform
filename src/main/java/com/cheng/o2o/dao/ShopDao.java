package com.cheng.o2o.dao;

import com.cheng.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/3/29 10:08
 */
public interface ShopDao {

    /**
     * 新增店铺
     *
     * @param shop
     * @return effectedNum
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     *
     * @param shop
     * @return effectedNum
     */
    int updateShop(Shop shop);

    /**
     * 通过 shop id 查询店铺
     *
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);

    /**
     * 返回 queryShopList 总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /**
     * 分页查询店铺，可输入的条件有:店铺名(模糊)，店铺状态，店铺类别，区域id，owner
     *
     * @param shopCondition 查询条件
     * @param rowIndex      从第几行开始取数据
     * @param pageSize      返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);
}
