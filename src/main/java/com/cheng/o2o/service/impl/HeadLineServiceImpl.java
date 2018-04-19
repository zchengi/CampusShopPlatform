package com.cheng.o2o.service.impl;

import com.cheng.o2o.cache.JedisUtil;
import com.cheng.o2o.dao.HeadLineDao;
import com.cheng.o2o.entity.HeadLine;
import com.cheng.o2o.exceptions.HeadLineOperationException;
import com.cheng.o2o.service.HeadLineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cheng
 *         2018/4/10 19:40
 */
@Service
public class HeadLineServiceImpl implements HeadLineService {

    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition)  {

        // 定义redis的 key 的前缀
        String key = HEADLINE_LIST_KEY;
        // 定义接收对象
        List<HeadLine> headLineList;
        // 定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        // 拼接出redis的 key
        if (headLineCondition != null && headLineCondition.getEnableStatus() != null) {
            key += "_" + headLineCondition.getEnableStatus();
        }

        // 判断 key 是否存在
        if (jedisKeys.exists(key)) {
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                headLineList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                logger.error(e.toString());
                e.printStackTrace();
                throw new HeadLineOperationException(e.getMessage());
            }
        } else {
            headLineList = headLineDao.queryHeadLine(headLineCondition);
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(headLineList);
            } catch (JsonProcessingException e) {
                logger.error(e.toString());
                e.printStackTrace();
                throw new HeadLineOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        }

        return headLineList;
    }
}
