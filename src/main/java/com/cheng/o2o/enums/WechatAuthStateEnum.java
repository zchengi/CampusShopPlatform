package com.cheng.o2o.enums;

/**
 * @author cheng
 *         2018/4/14 15:55
 */
public enum WechatAuthStateEnum {

    /**
     * 状态信息
     */
    LOGIN_FAIL(-1, "openId输入有误"),
    SUCCESS(0, "操作成功"),
    NULL_AUTH_INFO(-1006, "注册信息为空");

    private int state;

    private String stateInfo;

    WechatAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static WechatAuthStateEnum stateOf(int index) {
        for (WechatAuthStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }
}
