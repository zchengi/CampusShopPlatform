package com.cheng.o2o.service;

import com.cheng.o2o.dto.ShopExecution;
import com.cheng.o2o.entity.Shop;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

/**
 * @author cheng
 *         2018/3/29 15:11
 */
public interface ShopService {

    /**
     * 添加商铺
     *
     * @param shop
     * @param shopImg
     * @return
     */
    ShopExecution addShop(Shop shop, File shopImg);
}
