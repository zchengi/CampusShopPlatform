package com.cheng.o2o.enums;

/**
 * 帐号身份验证状态信息
 *
 * @author cheng
 *         2018/4/17 12:07
 */
public enum LocalAuthStateEnum {

    /**
     * 状态信息
     */
    LOGIN_FAIL(-1, "密码或帐号输入有误"),
    SUCCESS(0, "操作成功"),
    NULL_AUTH_INFO(-1006, "注册信息为空"),
    ONLY_ONE_ACCOUNT(-1007, "最多只能绑定一个本地帐号");


    private int state;
    private String stateInfo;


    LocalAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static LocalAuthStateEnum stateOf(int index) {
        for (LocalAuthStateEnum state : values()) {
            if (state.getState() == index) {
                return state;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
