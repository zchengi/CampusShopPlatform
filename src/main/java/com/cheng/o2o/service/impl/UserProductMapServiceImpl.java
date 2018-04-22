package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.UserProductMapDao;
import com.cheng.o2o.dto.UserProductMapExecution;
import com.cheng.o2o.entity.UserProductMap;
import com.cheng.o2o.service.UserProductMapService;
import com.cheng.o2o.util.PageCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cheng
 *         2018/4/22 14:55
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {

    @Autowired
    private UserProductMapDao userProductMapDao;

    @Override
    public UserProductMapExecution listUserProductMap(UserProductMap userProductMapCondition,
                                                      Integer pageIndex, Integer pageSize) {

        // 空值判断
        if (userProductMapCondition != null && pageIndex != null && pageSize != null) {
            // 页转行
            int beginIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
            // 根据查询条件分页取出列表
            List<UserProductMap> userProductMapList = userProductMapDao.
                    queryUserProductMapList(userProductMapCondition, beginIndex, pageSize);
            // 根据同等查询条件获取总数
            int count = userProductMapDao.queryUserProductMapCount(userProductMapCondition);

            UserProductMapExecution se = new UserProductMapExecution();
            se.setUserProductMapList(userProductMapList);
            se.setCount(count);
            return se;
        } else {
            return null;
        }
    }
}
