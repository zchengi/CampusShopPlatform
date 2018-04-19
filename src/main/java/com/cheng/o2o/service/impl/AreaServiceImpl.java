package com.cheng.o2o.service.impl;


import com.cheng.o2o.cache.JedisUtil;
import com.cheng.o2o.dao.AreaDao;
import com.cheng.o2o.entity.Area;
import com.cheng.o2o.exceptions.AreaOperationException;
import com.cheng.o2o.service.AreaService;
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
 *         2018/3/28 20:55
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;

    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Area> getAreaList() {

        // 定义redis的key
        String key = AREA_LIST_KEY;
        // 定义接收对象
        List<Area> areaList;
        // 定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        // 判断key是否存在
        if (jedisKeys.exists(key)) {
            // 若存在，则直接从redis里面取出相应数据
            String jsonString = jedisStrings.get(key);
            // 指定要将 string 转换成的集合类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
            try {
                // 将相关 key 对应的 value 里的 string 转换成对象的实体类集合
                areaList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                logger.error(e.toString());
                e.printStackTrace();
                throw new AreaOperationException(e.getMessage());
            }
        } else {
            // 若不存在，则从数据库里面取出相应数据
            areaList = areaDao.queryArea();
            // 将相关的实体类集合转换成 string ,存入redis里面对应的 key 中
            String jsonString;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                logger.error(e.toString());
                e.printStackTrace();
                throw new AreaOperationException(e.getMessage());
            }
            jedisStrings.set(key, jsonString);
        }

        return areaList;
    }
}
