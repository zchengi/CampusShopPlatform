package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.ShopAuthMapDao;
import com.cheng.o2o.dto.ShopAuthMapExecution;
import com.cheng.o2o.entity.ShopAuthMap;
import com.cheng.o2o.enums.ShopAuthMapStateEnum;
import com.cheng.o2o.exceptions.ShopAuthMapOperationException;
import com.cheng.o2o.service.ShopAuthMapService;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/4/20 19:48
 */
@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {

    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {

        // 空值判断，主要是对店铺id和员工id做校验
        if (shopAuthMap != null && shopAuthMap.getShop() != null && shopAuthMap.getShop().getShopId() != null
                && shopAuthMap.getEmployee() != null && shopAuthMap.getEmployee().getUserId() != null) {
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setTitleFlag(0);
            try {
                // 添加授权信息
                int effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
                if (effectedNum <= 0) {
                    throw new ShopAuthMapOperationException("添加授权信息失败!");
                }
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
            } catch (Exception e) {
                throw new ShopAuthMapOperationException("添加授权失败 : " + e.toString());
            }
        } else {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_ID);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopAuthMapExecution modifyShopAuthMap(ShopAuthMap shopAuthMap) throws ShopAuthMapOperationException {

        // 空值判断，主要是对授权id做校验
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_ID);
        } else {
            try {
                int effectedNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
                if (effectedNum <= 0) {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.INNER_ERROR);
                } else {
                    return new ShopAuthMapExecution(ShopAuthMapStateEnum.SUCCESS, shopAuthMap);
                }
            } catch (Exception e) {
                throw new ShopAuthMapOperationException("ModifyShopAuthMap error : " + e.toString());
            }
        }
    }

    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {

        // 空值判断
        if (shopId != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            // 查询返回该店铺的授权信息列表
            List<ShopAuthMap> shopAuthMapList = shopAuthMapDao.queryShopAuthMapListByShopId(shopId, beginIndex, pageSize);
            // 返回总数
            int count = shopAuthMapDao.queryShopAuthCountByShopId(shopId);
            ShopAuthMapExecution se = new ShopAuthMapExecution();
            se.setShopAuthMapList(shopAuthMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }
}
