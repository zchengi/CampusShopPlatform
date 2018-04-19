package com.cheng.o2o.dto;

import com.cheng.o2o.entity.WeChatAuth;
import com.cheng.o2o.enums.WechatAuthStateEnum;

import java.util.List;

/**
 * @author cheng
 *         2018/4/14 15:53
 */
public class WechatAuthExecution {

    /**
     * 结果状态
     */
    private int state;

    /**
     * 状态标识
     */
    private String stateInfo;

    private int count;

    private WeChatAuth wechatAuth;

    private List<WeChatAuth> wechatAuthList;

    public WechatAuthExecution() {
    }

    public WechatAuthExecution(WechatAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public WechatAuthExecution(WechatAuthStateEnum stateEnum, WeChatAuth wechatAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.wechatAuth = wechatAuth;
    }

    public WechatAuthExecution(WechatAuthStateEnum stateEnum,
                               List<WeChatAuth> wechatAuthList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.wechatAuthList = wechatAuthList;
    }

    public WechatAuthExecution(int state, String stateInfo, int count, WeChatAuth weChatAuth, List<WeChatAuth> weChatAuthList) {
        this.state = state;
        this.stateInfo = stateInfo;
        this.count = count;
        this.wechatAuth = weChatAuth;
        this.wechatAuthList = weChatAuthList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public WeChatAuth getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(WeChatAuth wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public List<WeChatAuth> getWechatAuthList() {
        return wechatAuthList;
    }

    public void setWechatAuthList(List<WeChatAuth> wechatAuthList) {
        this.wechatAuthList = wechatAuthList;
    }
}
