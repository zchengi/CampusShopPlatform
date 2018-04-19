package com.cheng.o2o.service;

import com.cheng.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * AreaService Tester.
 *
 * @author cheng
 * @version 1.2
 * @since <pre>03/28/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest  {

    @Autowired
    private AreaService areaService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        cacheService.removeFromCache(AreaService.AREA_LIST_KEY);
        areaService.getAreaList();
    }
}
