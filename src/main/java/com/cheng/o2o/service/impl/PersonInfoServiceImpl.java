package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.PersonInfoDao;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cheng
 *         2018/4/14 16:19
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {
        return personInfoDao.queryPersonInfoById(userId);
    }
}
