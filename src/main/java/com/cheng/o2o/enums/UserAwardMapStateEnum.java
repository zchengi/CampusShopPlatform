package com.cheng.o2o.enums;

/**
 * 用户购买商品相关信息
 *
 * @author cheng
 *         2018/4/23 13:20
 */
public enum UserAwardMapStateEnum {

    /**
     * 状态信息
     */
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    NULL_USER_AWARD_ID(-1002, " userAwardId 为空"),
    NULL_USER_AWARD_INFO(-1003, "传入了空的信息");

    private int state;
    private String stateInfo;

    UserAwardMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static UserAwardMapStateEnum stateInfo(int index) {
        for (UserAwardMapStateEnum state : values()) {
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
