package com.junyou.bus.xinmo.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xinmo_moshen_shiti")
public class RoleXinmoMoshenShiti extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("xinmo_moshen_id")
    private Integer xinmoMoshenId;

    @Column("shiti_time")
    private Long shitiTime;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getXinmoMoshenId() {
        return xinmoMoshenId;
    }

    public void setXinmoMoshenId(Integer xinmoMoshenId) {
        this.xinmoMoshenId = xinmoMoshenId;
    }

    public Long getShitiTime() {
        return shitiTime;
    }

    public void setShitiTime(Long shitiTime) {
        this.shitiTime = shitiTime;
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

    public RoleXinmoMoshenShiti copy() {
        RoleXinmoMoshenShiti result = new RoleXinmoMoshenShiti();
        result.setUserRoleId(getUserRoleId());
        result.setXinmoMoshenId(getXinmoMoshenId());
        result.setShitiTime(getShitiTime());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
