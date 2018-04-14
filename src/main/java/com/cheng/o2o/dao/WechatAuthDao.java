package com.cheng.o2o.dao;

import com.cheng.o2o.entity.WeChatAuth;

/**
 * @author cheng
 *         2018/4/14 15:14
 */
public interface WechatAuthDao {

    /**
     * 通过openId查询对应本平台的微信帐号
     *
     * @param openId
     * @return
     */
    WeChatAuth queryWechatInfoByOpenId(String openId);

    /**
     * 添加对应本平台的微信帐号
     *
     * @param weChatAuth
     * @return
     */
    int insertWechatAuth(WeChatAuth weChatAuth);
}
