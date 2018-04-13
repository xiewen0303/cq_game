package com.junyou.bus.kuafu_yungong.entity;

import java.io.Serializable;

/**
 * 
 *@Description 跨服云宫之战活动结果数据
 *@Author Yang Gao
 *@Since 2016-9-20
 *@Version 1.1.0
 */
public class KuafuYunGongResult implements Serializable {

    private static final long serialVersionUID = 6012408817395872413L;

    private Long guildLeaderId;

    private String guildName;

    private Long guildId;

    private Long updateTime;

    public Long getGuildLeaderId() {
        return guildLeaderId;
    }

    public void setGuildLeaderId(Long guildLeaderId) {
        this.guildLeaderId = guildLeaderId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

}
