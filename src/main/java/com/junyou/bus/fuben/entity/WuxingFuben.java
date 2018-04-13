package com.junyou.bus.fuben.entity;

import java.io.Serializable;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("wuxing_fuben")
public class WuxingFuben extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("fuben_info")
    private String fubenInfo;

    @Column("fight_count")
    private Integer fightCount;

    @Column("buy_count")
    private Integer buyCount;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    @EntityField
    private int state = GameConstants.FUBEN_STATE_READY;

    @EntityField
    private int curFubenId;

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getFubenInfo() {
        return fubenInfo;
    }

    public void setFubenInfo(String fubenInfo) {
        this.fubenInfo = fubenInfo;
    }

    public Integer getFightCount() {
        return fightCount;
    }

    public void setFightCount(Integer fightCount) {
        this.fightCount = fightCount;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCurFubenId() {
        return curFubenId;
    }

    public void setCurFubenId(int curFubenId) {
        this.curFubenId = curFubenId;
    }

    @Override
    public String getPirmaryKeyName() {
        return "userRoleId";
    }

    @Override
    public Long getPrimaryKeyValue() {
        return userRoleId;
    }

    public WuxingFuben copy() {
        WuxingFuben result = new WuxingFuben();
        result.setUserRoleId(getUserRoleId());
        result.setFubenInfo(getFubenInfo());
        result.setFightCount(getFightCount());
        result.setBuyCount(getBuyCount());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
