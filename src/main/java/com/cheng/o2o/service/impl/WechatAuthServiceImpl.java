package com.cheng.o2o.service.impl;

import com.cheng.o2o.dao.PersonInfoDao;
import com.cheng.o2o.dao.WechatAuthDao;
import com.cheng.o2o.dto.WechatAuthExecution;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.WeChatAuth;
import com.cheng.o2o.enums.WechatAuthStateEnum;
import com.cheng.o2o.exceptions.WechatAuthOperationException;
import com.cheng.o2o.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author cheng
 *         2018/4/14 15:59
 */
@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    private static Logger logger = LoggerFactory.getLogger(WechatAuthServiceImpl.class);

    @Autowired
    private WechatAuthDao wechatAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WeChatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WechatAuthExecution register(WeChatAuth weChatAuth) throws WechatAuthOperationException {

        // 空值判断
        if (weChatAuth == null || weChatAuth.getOpenId() == null) {
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }

        try {
            // 设置创建时间
            weChatAuth.setCreateTime(new Date());
            // 如果微信帐号里夹带着用户信息并且用户id为空，则认为该用户第一次访问平台（且通过微信登录）
            // 则自动创建用户信息
            if (weChatAuth.getPersonInfo() != null && weChatAuth.getPersonInfo().getUserId() == null) {
                try {
                    weChatAuth.getPersonInfo().setCreateTime(new Date());
                    weChatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = weChatAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    if (effectedNum <= 0) {
                        throw new WechatAuthOperationException("添加用户失败信息。");
                    }
                } catch (Exception e) {
                    logger.error("insertPersonInfo error : " + e.toString());
                    throw new WechatAuthOperationException("insertPersonInfo error : " + e.getMessage());
                }
            }

            // 创建只属于本平台的微信登录帐号
            int effectedNum = wechatAuthDao.insertWechatAuth(weChatAuth);
            if (effectedNum <= 0) {
                throw new WechatAuthOperationException("帐号创建失败。");
            } else {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, weChatAuth);
            }
        } catch (Exception e) {
            logger.error("insertWechatAuth error : " + e.toString());
            throw new WechatAuthOperationException("insertWechatAuth error : " + e.getMessage());
        }
    }
}
