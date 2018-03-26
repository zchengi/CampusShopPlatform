package com.cheng.o2o.entity;

import javax.xml.crypto.Data;

/**
 * 用户
 *
 * @author cheng
 *         2018/3/26 20:43
 */
public class PersonInfo {

    /**
     * ID
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String profileImg;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private String gender;
    /**
     * 状态
     * 0:不可用,1:可用
     */
    private Integer enableStatus;
    /**
     * 用户类别
     * 1:顾客,2:店家,3:超级管理员
     */
    private Integer userType;
    /**
     * 创建时间
     */
    private Data createTime;
    /**
     * 修改时间
     */
    private Data lastEditTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Data getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Data createTime) {
        this.createTime = createTime;
    }

    public Data getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Data lastEditTime) {
        this.lastEditTime = lastEditTime;
    }
}
