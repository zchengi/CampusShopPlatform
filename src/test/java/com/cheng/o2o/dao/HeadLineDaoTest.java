package com.cheng.o2o.dao;

import com.cheng.o2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * HeadLineDao Tester.
 *
 * @author cheng
 * @version 1.0
 * @since <pre>04/19/2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest  {

    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testQueryHeadLine() {
        List<HeadLine> headLineList = headLineDao.queryHeadLine(new HeadLine());
        assertEquals(4, headLineList.size());
    }
}