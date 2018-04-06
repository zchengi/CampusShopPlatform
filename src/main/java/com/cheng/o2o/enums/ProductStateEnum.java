package com.cheng.o2o.enums;

/**
 * 商品状态信息
 *
 * @author cheng
 *         2018/4/5 23:06
 */
public enum ProductStateEnum {

    /**
     * 状态信息
     */
    OFFLINE(-1, "非法商品"),
    SUCCESS(0, "操作成功"),
    PASS(2, "通过认证"),
    INNER_ERROR(-1001, "操作失败"),
    Empty(-1002, "商品为空");

    private int state;
    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据传入的state返回相应的enum值
     */
    public static ProductStateEnum stateOf(int index) {
        for (ProductStateEnum state : values()) {
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
