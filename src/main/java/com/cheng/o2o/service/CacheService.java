package com.cheng.o2o.service;

/**
 * @author cheng
 *         2018/4/16 20:10
 */
public interface CacheService {
    /**
     * 根据 key 前缀删除匹配该模式下的所有 key-value
     * 如传入: shopCategoryList ,则 shopCategoryList_allFirstLevel 等以 shopCategoryList 打头的 key_value 都会被清空
     *
     * @param keyPrefix
     * @return
     */
    void removeFromCache(String keyPrefix);
}
