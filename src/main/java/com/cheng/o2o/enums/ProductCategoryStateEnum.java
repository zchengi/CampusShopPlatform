package com.cheng.o2o.enums;

/**
 * 店铺商品类别状态信息
 *
 * @author cheng
 *         2018/4/4 14:42
 */
public enum ProductCategoryStateEnum {

    /**
     * 状态信息
     */
    SUCCESS(1, "创建成功"),
    INNER_ERROR(-1001, "操作失败"),
    EMPTY_LIST(-1002, "添加数少于1");

    private int state;

    private String stateInfo;

    ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ProductCategoryStateEnum stateOf(int index) {
        for (ProductCategoryStateEnum state : values()) {
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
