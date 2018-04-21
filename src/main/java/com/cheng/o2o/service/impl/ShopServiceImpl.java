package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.ShopAuthMapDao;
import com.cheng.o2o.dao.ShopDao;
import com.cheng.o2o.dto.ImageHolder;
import com.cheng.o2o.dto.ShopExecution;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.ShopAuthMap;
import com.cheng.o2o.enums.ShopStateEnum;
import com.cheng.o2o.exceptions.ShopOperationException;
import com.cheng.o2o.service.ShopService;
import com.cheng.o2o.util.FileUtil;
import com.cheng.o2o.util.ImgUtil;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author cheng
 *         2018/3/29 15:13
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {

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
                if (thumbnail.getImage() != null) {
                    // 2.存储图片
                    try {
                        addShopImg(shop, thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg() err:" + e.getMessage());
                    }

                    // 3.更新店铺图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }

                    // 4.执行增加shopAuthMao操作
                    ShopAuthMap shopAuthMap = new ShopAuthMap();
                    shopAuthMap.setEmployee(shop.getOwner());
                    shopAuthMap.setShop(shop);
                    shopAuthMap.setTitle("店家");
                    shopAuthMap.setTitleFlag(0);
                    shopAuthMap.setCreateTime(new Date());
                    shopAuthMap.setLastEditTime(new Date());
                    shopAuthMap.setEnableStatus(1);
                    try {
                        effectedNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);

                        if (effectedNum <= 0) {
                            throw new ShopOperationException("授权创建失败!");
                        }
                    } catch (Exception e) {
                        throw new ShopOperationException("insertShopAuthMap error : " + e.getMessage());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) throws ShopOperationException {

        try {
            if (shop == null || shop.getShopId() == null) {
                return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
            } else {
                // 1.判断是否需要处理图片
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImgUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, thumbnail);

                }

                // 2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("modifyShop error: " + e.getMessage());
        }
    }

    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        // 将页码转换成行码
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        // 根据查询条件，调用dao层返回相关的店铺列表
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        // 根据相同的查询条件，返回店铺总数
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }

        return se;
    }

    private void addShopImg(Shop shop, ImageHolder thumbnail) {
        // 获取shop图片目录的相对子路径
        String dest = FileUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImgUtil.generateThumbnail(thumbnail, dest);
        shop.setShopImg(shopImgAddr);
    }
}
