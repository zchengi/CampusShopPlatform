package com.cheng.o2o.dto;

import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.enums.UserShopMapStateEnum;

import java.util.List;

/**
 * @author cheng
 *         2018/4/23 12:17
 */
public class UserShopMapExecution {

    /**
     * 结果状态
     */
    private int state;
    /**
     * 状态标识
     */
    private String stateInfo;
    /**
     * 授权数
     */
    private Integer count;
    /**
     * 操作的 userProductMap
     */
    private UserShopMap userShopMap;
    /**
     * 用户积分列表(查询专用)
     */
    private List<UserShopMap> userShopMapList;

    public UserShopMapExecution() {
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum, UserShopMap userShopMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum, List<UserShopMap> userShopMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMapList = userShopMapList;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }
}
