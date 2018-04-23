package com.cheng.o2o.dto;

import com.cheng.o2o.entity.Award;
import com.cheng.o2o.enums.AwardStateEnum;

import java.util.List;

/**
 * @author cheng
 *         2018/4/23 13:51
 */
public class AwardExecution {

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
    private Award award;
    /**
     * 用户兑换礼品(查询专用)
     */
    private List<Award> userAwardMapList;

    public AwardExecution() {
    }

    public AwardExecution(AwardStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public AwardExecution(AwardStateEnum stateEnum, Award award) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.award = award;
    }

    public AwardExecution(AwardStateEnum stateEnum, List<Award> userAwardMapList) {
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public List<Award> getUserAwardMapList() {
        return userAwardMapList;
    }

    public void setUserAwardMapList(List<Award> userAwardMapList) {
        this.userAwardMapList = userAwardMapList;
    }
}
