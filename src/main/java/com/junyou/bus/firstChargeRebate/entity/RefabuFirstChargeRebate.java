package com.junyou.bus.firstChargeRebate.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refabu_first_charge_rebate")
public class RefabuFirstChargeRebate extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("id")
    private Long id;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("sub_id")
    private Integer subId;

    @Column("first_gold")
    private Integer firstGold;

    @Column("rebate_ratio")
    private Float rebateRatio;

    @Column("rebate_gold")
    private Integer rebateGold;

    @Column("activity_status")
    private Integer activityStatus;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public Integer getFirstGold() {
        return firstGold;
    }

    public void setFirstGold(Integer firstGold) {
        this.firstGold = firstGold;
    }

    public Float getRebateRatio() {
        return rebateRatio;
    }

    public void setRebateRatio(Float rebateRatio) {
        this.rebateRatio = rebateRatio;
    }

    public Integer getRebateGold() {
        return rebateGold;
    }

    public void setRebateGold(Integer rebateGold) {
        this.rebateGold = rebateGold;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
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
        return "id";
    }

    @Override
    public Long getPrimaryKeyValue() {
        return id;
    }

    public RefabuFirstChargeRebate copy() {
        RefabuFirstChargeRebate result = new RefabuFirstChargeRebate();
        result.setId(getId());
        result.setUserRoleId(getUserRoleId());
        result.setSubId(getSubId());
        result.setFirstGold(getFirstGold());
        result.setRebateRatio(getRebateRatio());
        result.setRebateGold(getRebateGold());
        result.setActivityStatus(getActivityStatus());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
