package com.cheng.o2o.dao;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.Area;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AreaDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>03/28/2018</pre>
 */
public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void queryArea() {
        List<Area> areaList = areaDao.queryArea();
        Assert.assertEquals(2, areaList.size());
    }
}