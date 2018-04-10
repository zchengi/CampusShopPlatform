package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.HeadLineDao;
import com.cheng.o2o.entity.HeadLine;
import com.cheng.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cheng
 *         2018/4/10 19:40
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition)  {
        return headLineDao.queryHeadLine(headLineCondition);
    }
}
