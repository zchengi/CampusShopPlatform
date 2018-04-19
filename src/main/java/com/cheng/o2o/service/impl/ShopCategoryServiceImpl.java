package com.cheng.o2o.service.impl;

import com.cheng.o2o.cache.JedisUtil;
import com.cheng.o2o.dao.ShopCategoryDao;
import com.cheng.o2o.entity.ShopCategory;
import com.cheng.o2o.exceptions.ShopCategoryOperationException;
import com.cheng.o2o.service.ShopCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cheng
 *         2018/3/31 13:05
 */
@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) {

        String key = SHOP_CATEGORY_LIST_KEY;
        List<ShopCategory> shopCategoryList;
        ObjectMapper mapper = new ObjectMapper();

        if (shopCategoryCondition == null) {
            // 若查询条件为空，则列出所有首页大类，即 parentId 为空的店铺类别
            key += "_allFirstLevel";
        } else if (shopCategoryCondition != null && shopCategoryCondition.getParent() != null
                && shopCategoryCondition.getParent().getShopCategoryId() != null) {
            // 若 parentId 为空，则列出该 parentId 下的所有子类别
            key += "_parent" + shopCategoryCondition.getParent().getShopCategoryId();
        } else if (shopCategoryCondition != null) {
            // 列出所有子类别，不管其属于哪个类
            key += "allSecondLevel";
        }

        if (jedisKeys.exists(key)) {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                throw new ShopCategoryOperationException(e.getMessage());
            }
        } else {
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(shopCategoryList);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
                throw new ShopCategoryOperationException(e.getMessage());
            }

            jedisStrings.set(key, jsonString);
        }

        return shopCategoryList;
    }
}
