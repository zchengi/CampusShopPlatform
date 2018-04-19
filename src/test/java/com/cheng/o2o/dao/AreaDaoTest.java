package com.cheng.o2o.dao;

import com.cheng.o2o.entity.Area;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * AreaDao Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>03/28/2018</pre>
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaDaoTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea() {
        List<Area> areaList = areaDao.queryArea();
        Assert.assertEquals(4, areaList.size());
    }
}