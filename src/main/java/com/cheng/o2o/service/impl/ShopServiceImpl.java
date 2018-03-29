package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.ShopDao;
import com.cheng.o2o.dto.ShopExecution;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.enums.ShopStateEnum;
import com.cheng.o2o.exceptions.ShopOperationException;
import com.cheng.o2o.service.ShopService;
import com.cheng.o2o.util.FileUtil;
import com.cheng.o2o.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.util.Date;

/**
 * @author cheng
 *         2018/3/29 15:13
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional(rollbackFor = ShopOperationException.class)
    public ShopExecution addShop(Shop shop, File shopImg) {

        // 空值判断
        if (shop == null || shop.getOwner() == null || shop.getArea() == null || shop.getShopCategory() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }

        try {
            // 店铺信息赋初始值
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            // 1.添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum > 0) {
                if (shopImg != null) {
                    // 2.存储图片
                    try {
                        addShopImg(shop, shopImg);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg() err:" + e.getMessage());
                    }

                    // 3.更新店铺图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            } else {
                throw new ShopOperationException("店铺创建失败");
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop() error:" + e.getMessage());
        }

        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop, File shopImg) {
        // 获取shop图片目录的相对子路径
        String dest = FileUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImgUtil.generateThumbnail(shopImg, dest);
        shop.setShopImg(shopImgAddr);
    }
}
