package com.junyou.bus.kuafu_yungong.entity;

import java.io.Serializable;

/**
 * 
 * @Description 跨服云宫之巅活动奖励数据
 * @Author Yang Gao
 * @Since 2016-9-28
 * @Version 1.1.0
 */
public class KuafuYunGongDayReward implements Serializable {

    private static final long serialVersionUID = 2989358803319111017L;

    private Long userRoleId;

    private Integer sex;

    private String name;

    private Integer position;

    private Long guildId;

    private Integer state;

    private Long updateTime;

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

}
