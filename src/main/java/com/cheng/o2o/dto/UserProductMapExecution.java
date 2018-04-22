package com.cheng.o2o.dto;

import com.cheng.o2o.entity.UserProductMap;
import com.cheng.o2o.enums.UserProductMapStateEnum;

import java.util.List;

/**
 * @author cheng
 *         2018/4/22 14:53
 */
public class UserProductMapExecution {

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
    private UserProductMap userProductMap;
    /**
     * 授权列表(查询专用)
     */
    private List<UserProductMap> userProductMapList;

    public UserProductMapExecution() {
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum, UserProductMap userProductMap) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMap = userProductMap;
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum, List<UserProductMap> userProductMapList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMapList = userProductMapList;
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

    public UserProductMap getUserProductMap() {
        return userProductMap;
    }

    public void setUserProductMap(UserProductMap userProductMap) {
        this.userProductMap = userProductMap;
    }

    public List<UserProductMap> getUserProductMapList() {
        return userProductMapList;
    }

    public void setUserProductMapList(List<UserProductMap> userProductMapList) {
        this.userProductMapList = userProductMapList;
    }
}
