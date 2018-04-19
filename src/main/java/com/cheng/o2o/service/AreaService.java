package com.cheng.o2o.service;

import com.cheng.o2o.entity.Area;

import java.util.List;

/**
 * @author cheng
 *         2018/3/28 20:55
 */
public interface AreaService {

    /**
     * 当前类在 redis 中存储的 key
     */
    String AREA_LIST_KEY = "areaList";

    /**
     * 获取区域列表
     *
     * @return areaList
     */
    List<Area> getAreaList();
}
