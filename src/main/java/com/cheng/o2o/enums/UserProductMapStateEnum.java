package com.cheng.o2o.enums;

/**
 * 用户消费相关状态信息
 *
 * @author cheng
 *         2018/4/22 15:00
 */
public enum UserProductMapStateEnum {

    /**
     * 状态信息
     */
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    NULL_USER_PRODUCT_ID(-1002, "userProductId 为空"),
    NULL_USER_PRODUCT_INFO(-1003, "传入了空的信息");

    private int state;
    private String stateInfo;

    UserProductMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static UserProductMapStateEnum stateInfo(int index) {
        for (UserProductMapStateEnum state : values()) {
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
