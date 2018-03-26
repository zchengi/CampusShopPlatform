package com.cheng.o2o.entity;

import java.util.Date;

/**
 * 微信帐号
 *
 * @author cheng
 *         2018/3/26 21:01
 */
public class WeChatAuth {

    /**
     * 微信帐号
     */
    private Long wechatAuthId;
    /**
     * openID
     */
    private String openId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 用户信息
     */
    private PersonInfo personInfo;

    public Long getWechatAuthId() {
        return wechatAuthId;
    }

    public void setWechatAuthId(Long wechatAuthId) {
        this.wechatAuthId = wechatAuthId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }
}
