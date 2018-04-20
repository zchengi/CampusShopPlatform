package com.cheng.o2o.enums;

/**
 * 店铺授权相关状态信息
 *
 * @author cheng
 *         2018/4/20 19:36
 */
public enum ShopAuthMapStateEnum {

    /**
     * 状态信息
     */
    SUCCESS(1, "操作成功"),
    INNER_ERROR(-1001, "操作失败"),
    NULL_SHOP_AUTH_ID(-1002, "ShopAuthId 为空"),
    NULL_SHOP_AUTH_INFO(-1003, "传入了空的信息");

    private int state;
    private String stateInfo;

    ShopAuthMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopAuthMapStateEnum stateInfo(int index) {
        for (ShopAuthMapStateEnum state : values()) {
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
