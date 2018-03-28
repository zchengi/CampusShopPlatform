package com.cheng.o2o.dao;

import com.cheng.o2o.entity.Area;

import java.util.List;

/**
 * @author cheng
 *         2018/3/28 20:10
 */
public interface AreaDao {

    /**
     * 列出区域列表
     *
     * @return areaList
     */
    List<Area> queryArea();
}
