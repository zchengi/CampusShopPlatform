package com.cheng.o2o.service;

import com.cheng.o2o.entity.HeadLine;

import java.io.IOException;
import java.util.List;

/**
 * @author cheng
 *         2018/4/10 19:39
 */
public interface HeadLineService {

    /**
     * 根据传入的条件返回指定的头条列表
     *
     * @param headLineCondition
     * @return
     * @throws IOException
     */
    List<HeadLine> getHeadLineList(HeadLine headLineCondition);
}
