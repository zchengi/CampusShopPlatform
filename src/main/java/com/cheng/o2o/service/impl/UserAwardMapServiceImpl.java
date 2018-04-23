package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.UserAwardMapDao;
import com.cheng.o2o.dao.UserShopMapDao;
import com.cheng.o2o.dto.UserAwardMapExecution;
import com.cheng.o2o.entity.UserAwardMap;
import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.enums.UserAwardMapStateEnum;
import com.cheng.o2o.exceptions.UserAwardMapOperationException;
import com.cheng.o2o.service.UserAwardMapService;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/23 13:25
 */
@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {

    @Autowired
    private UserShopMapDao userShopMapDao;
    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {

        // 空值判断
        if (userAwardMap != null && userAwardMap.getUser() != null && userAwardMap.getUser().getUserId() != null
                && userAwardMap.getShop() != null && userAwardMap.getShop().getShopId() != null) {

            // 设置默认值
            userAwardMap.setCreateTime(new Date());
            userAwardMap.setUsedStatus(0);
            try {
                int effectedNum;
                // 如果该奖品需要消耗积分，则将 tb_user_shop_map 对应的用户积分抵扣
                if (userAwardMap.getPoint() != null && userAwardMap.getPoint() > 0) {
                    // 根据用户id和店铺id获取用户在该店铺下的积分
                    UserShopMap userShopMap = userShopMapDao.
                            queryUserShopMap(userAwardMap.getUser().getUserId(), userAwardMap.getShop().getShopId());
                    // 判断该用户在店铺里是否有积分
                    // 如果有积分，必须保证用户积分大于本次兑换所需积分
                    if (userShopMap != null && userShopMap.getPoint() > userAwardMap.getPoint()) {
                        // 积分抵扣
                        userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
                        // 更新积分信息
                        effectedNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if (effectedNum <= 0) {
                            throw new UserAwardMapOperationException("更新积分信息失败!");
                        }
                    } else {
                        throw new UserAwardMapOperationException("积分不足无法兑换!");
                    }
                }

                // 插入礼品兑换信息
                effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
                if (effectedNum <= 0) {
                    throw new UserAwardMapOperationException("领取礼品失败!");
                }

                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new UserAwardMapOperationException("领取礼品失败!");
            }
        } else {
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_USER_AWARD_INFO);
        }
    }

    @Override
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) throws UserAwardMapOperationException {
        return null;
    }

    @Override
    public UserAwardMapExecution listUserAwardMap(UserAwardMap userAwardMapCondition,
                                                  Integer pageIndex, Integer pageSize) {

        // 空值判断
        if (userAwardMapCondition != null && pageIndex != -1 && pageSize != -1) {
            // 页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            // 根据传入的查询条件分页返回用户积分列表信息
            List<UserAwardMap> userAwardMapList = userAwardMapDao.
                    queryUserAwardMapList(userAwardMapCondition, beginIndex, pageSize);
            // 返回总数
            int count = userAwardMapDao.queryUserAwardMapCount(userAwardMapCondition);

            UserAwardMapExecution ue = new UserAwardMapExecution();
            ue.setUserAwardMapList(userAwardMapList);
            ue.setCount(count);
            return ue;
        } else {
            return null;
        }
    }

    @Override
    public UserAwardMap getUserAwardMapById(long userAwardMapId) {
        return userAwardMapDao.queryUserAwardMapById(userAwardMapId);
    }
}
