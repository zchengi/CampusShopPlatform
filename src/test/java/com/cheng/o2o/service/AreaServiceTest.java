package com.cheng.o2o.service;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.Area;
import com.cheng.o2o.service.AreaService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AreaService Tester.
 *
 * @author cheng
 * @version 1.1
 * @since <pre>03/28/2018</pre>
 */
public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        // Assert.assertEquals("西苑", areaList.get(1).getAreaName());
        // Assert.assertEquals((long) 3, (long) areaList.get(1).getPriority());
        cacheService.removeFromCache(AreaService.AREA_LIST_KEY);
        areaService.getAreaList();
    }
}
