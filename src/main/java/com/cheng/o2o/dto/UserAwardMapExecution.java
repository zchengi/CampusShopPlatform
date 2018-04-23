package com.cheng.o2o.dto;

import com.cheng.o2o.entity.UserAwardMap;
import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.enums.UserAwardMapStateEnum;
import com.cheng.o2o.enums.UserShopMapStateEnum;

import java.util.List;

/**
 * @author cheng
 *         2018/4/23 13:22
 */
public class UserAwardMapExecution {

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
     * 操作的 userAwardMap
     */
    private UserAwardMap userAwardMap;
    /**
     * 用户兑换礼品(查询专用)
     */
    private List<UserAwardMap> userAwardMapList;

    public UserAwardMapExecution() {
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, UserAwardMap userAwardMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMap = userAwardMap;
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, List<UserAwardMap> userAwardMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMapList = userAwardMapList;
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

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<UserAwardMap> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }
}
