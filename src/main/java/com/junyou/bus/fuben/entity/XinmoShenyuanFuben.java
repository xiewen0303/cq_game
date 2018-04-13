package com.junyou.bus.fuben.entity;

import java.io.Serializable;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("xinmo_shenyuan_fuben")
public class XinmoShenyuanFuben extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("pass_fuben_id")
    private Integer passFubenId;

    @Column("fail_boss_type")
    private Integer failBossType;

    @Column("cooling_time")
    private Long coolingTime;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    @EntityField
    private int coolingStatus = GameConstants.XM_SHENYUAN_STATUS_COOLING_NO;// 副本cd冷却状态

    @EntityField
    private int fubenStatus = GameConstants.FUBEN_STATE_READY;// 副本挑战状态


    public int getFubenStatus() {
        return fubenStatus;
    }

    public void setFubenStatus(int fubenStatus) {
        this.fubenStatus = fubenStatus;
    }
    
    

    public int getCoolingStatus() {
        return coolingStatus;
    }

    public void setCoolingStatus(int coolingStatus) {
        this.coolingStatus = coolingStatus;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getPassFubenId() {
        return passFubenId;
    }

    public void setPassFubenId(Integer passFubenId) {
        this.passFubenId = passFubenId;
    }

    public Integer getFailBossType() {
        return failBossType;
    }

    public void setFailBossType(Integer failBossType) {
        this.failBossType = failBossType;
    }

    public Long getCoolingTime() {
        return coolingTime;
    }

    public void setCoolingTime(Long coolingTime) {
        this.coolingTime = coolingTime;
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

    @Override
    public String getPirmaryKeyName() {
        return "userRoleId";
    }

    @Override
    public Long getPrimaryKeyValue() {
        return userRoleId;
    }

    public XinmoShenyuanFuben copy() {
        XinmoShenyuanFuben result = new XinmoShenyuanFuben();
        result.setUserRoleId(getUserRoleId());
        result.setPassFubenId(getPassFubenId());
        result.setFailBossType(getFailBossType());
        result.setCoolingTime(getCoolingTime());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
