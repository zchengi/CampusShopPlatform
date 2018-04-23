package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.UserProductMapDao;
import com.cheng.o2o.dao.UserShopMapDao;
import com.cheng.o2o.dto.UserProductMapExecution;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.UserProductMap;
import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.enums.UserProductMapStateEnum;
import com.cheng.o2o.exceptions.UserProductMapOperationException;
import com.cheng.o2o.service.UserProductMapService;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/22 14:55
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {

    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) throws UserProductMapOperationException {

        // 空值判断 主要确保顾客id，店铺id以及操作员id非空
        if (userProductMap != null && userProductMap.getUser() != null && userProductMap.getUser().getUserId() != null
                && userProductMap.getShop() != null && userProductMap.getShop().getShopId() != null
                && userProductMap.getOperator() != null) {

            // 设置默认值
            userProductMap.setCreateTime(new Date());
            try {
                int effectNum = userProductMapDao.insertProductMap(userProductMap);
                if (effectNum <= 0) {
                    throw new UserProductMapOperationException("添加消费记录失败!");
                }

                // 如果本次消费能够积分
                if (userProductMap.getPoint() != null && userProductMap.getPoint() > 0) {
                    UserShopMap userShopMap = userShopMapDao.
                            queryUserShopMap(userProductMap.getUser().getUserId(), userProductMap.getShop().getShopId());
                    if (userShopMap != null && userShopMap.getUserShopId() != null) {
                        // 如果之前消费过，即有积分记录，则进行总积分更新操作
                        userShopMap.setPoint(userShopMap.getPoint() + userProductMap.getPoint());
                        effectNum = userShopMapDao.updateUserShopMapPoint(userShopMap);
                        if (effectNum <= 0) {
                            throw new UserProductMapOperationException("更新积分信息失败!");
                        }
                    } else {
                        // 没有消费过
                        userShopMap = compactUserShopMap3Add(userProductMap.getUser().getUserId(),
                                userProductMap.getShop().getShopId(), userProductMap.getPoint());
                        effectNum = userShopMapDao.insertUserShopMap(userShopMap);
                        if (effectNum <= 0) {
                            throw new UserProductMapOperationException("创建积分信息失败!");
                        }
                    }
                }
                return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, userProductMap);
            } catch (Exception e) {
                throw new UserProductMapOperationException("添加授权信息失败 : " + e.getMessage());
            }
        } else {
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_USER_PRODUCT_INFO);
        }
    }

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition,
                                                      Integer pageIndex, Integer pageSize) {

        // 空值判断
        if (userProductMapCondition != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            // 根据查询条件分页取出列表
            List<UserProductMap> userProductMapList = userProductMapDao.
                    queryUserProductMapList(userProductMapCondition, beginIndex, pageSize);
            // 根据同等查询条件获取总数
            int count = userProductMapDao.queryUserProductMapCount(userProductMapCondition);

            UserProductMapExecution se = new UserProductMapExecution();
            se.setUserProductMapList(userProductMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }
    }

    /**
     * 封装顾客信息
     *
     * @param userId
     * @param shopId
     * @param point
     * @return
     */
    private UserShopMap compactUserShopMap3Add(Long userId, Long shopId, Integer point) {

        UserShopMap userShopMap = null;

        if (userId != null && shopId != null) {
            userShopMap = new UserShopMap();

            PersonInfo customer = new PersonInfo();
            customer.setUserId(userId);
            userShopMap.setUser(customer);

            Shop shop = new Shop();
            shop.setShopId(shopId);
            userShopMap.setShop(shop);

            userShopMap.setPoint(point);
            userShopMap.setCreateTime(new Date());
        }

        return userShopMap;
    }
}
