package com.cheng.o2o.dao;

import com.cheng.o2o.entity.Award;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cheng
 *         2018/4/20 11:21
 */
public interface AwardDao {

    /**
     * 添加奖品信息
     *
     * @param award
     * @return
     */
    int insertAward(Award award);

    /**
     * 删除奖品信息
     *
     * @param awardId
     * @param shopId
     * @return
     */
    int deleteAward(@Param("awardId") long awardId, @Param("shopId") long shopId);

    /**
     * 更新奖品信息
     *
     * @param award
     * @return
     */
    int updateAward(Award award);

    /**
     * 根据传入的查询条件分页显示奖品信息列表
     *
     * @param awardCondition
     * @param pageSize
     * @param rowIndex
     * @return
     */
    List<Award> queryAwardList(@Param("awardCondition") Award awardCondition,
                               @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 配合 queryAwardList 返回相同查询条件下的奖品数
     *
     * @param awardCondition
     * @return
     */
    int queryAwardCount(@Param("awardCondition") Award awardCondition);

    /**
     * 通过 awardId 查询奖品信息
     *
     * @param awardId
     * @return
     */
    Award queryAwardByAwardId(long awardId);
}
