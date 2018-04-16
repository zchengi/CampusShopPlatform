package com.cheng.o2o.service;

import com.cheng.o2o.BaseTest;
import com.cheng.o2o.entity.HeadLine;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;


/**
 * HeadLineService Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/16/2018</pre>
 */
public class HeadLineServiceTest extends BaseTest {

    @Autowired
    private HeadLineService headLineService;

    @Test
    public void testGetHeadLineList() {

        List<HeadLine> headLineList = headLineService.getHeadLineList(new HeadLine());
        System.out.println(headLineList.get(0).getLineName());

        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        headLineList = headLineService.getHeadLineList(headLineCondition);
        System.out.println(headLineList.get(0).getLineName());
    }
}