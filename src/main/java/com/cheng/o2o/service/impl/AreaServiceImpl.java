package com.cheng.o2o.service.impl;


import com.cheng.o2o.dao.AreaDao;
import com.cheng.o2o.entity.Area;
import com.cheng.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cheng
 *         2018/3/28 20:55
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
