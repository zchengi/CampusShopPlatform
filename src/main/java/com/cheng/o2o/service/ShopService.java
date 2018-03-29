package com.cheng.o2o.service;

import com.cheng.o2o.dto.ShopExecution;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.exceptions.ShopOperationException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * @author cheng
 *         2018/3/29 15:11
 */
public interface ShopService {

    /**
     * 添加商铺
     *
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName)
            throws ShopOperationException;
}
