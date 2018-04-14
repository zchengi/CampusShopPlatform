package com.cheng.o2o.service;

import com.cheng.o2o.dto.WechatAuthExecution;
import com.cheng.o2o.entity.WeChatAuth;
import com.cheng.o2o.exceptions.WechatAuthOperationException;

/**
 * @author cheng
 *         2018/4/14 15:51
 */
public interface WechatAuthService {

    /**
     * 通过 openId 查询平台对应的微信帐号
     *
     * @param openId
     * @return
     */
    WeChatAuth getWechatAuthByOpenId(String openId);

    /**
     * 使用微信帐号注册
     *
     * @param weChatAuth
     * @return
     * @throws WechatAuthOperationException
     */
    WechatAuthExecution register(WeChatAuth weChatAuth) throws WechatAuthOperationException;
}
