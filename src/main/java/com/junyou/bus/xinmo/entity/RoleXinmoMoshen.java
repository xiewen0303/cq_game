package com.junyou.bus.xinmo.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xinmo_moshen")
public class RoleXinmoMoshen extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("id")
    private Long id;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("xm_moshen_type")
    private Integer xmMoshenType;

    @Column("xm_moshen_rank")
    private Integer xmMoshenRank;

    @Column("bless_value")
    private Integer blessValue;

    @Column("bless_clear_time")
    private Long blessClearTime;

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

    public Integer getXmMoshenType() {
        return xmMoshenType;
    }

    public void setXmMoshenType(Integer xmMoshenType) {
        this.xmMoshenType = xmMoshenType;
    }

    public Integer getXmMoshenRank() {
        return xmMoshenRank;
    }

    public void setXmMoshenRank(Integer xmMoshenRank) {
        this.xmMoshenRank = xmMoshenRank;
    }

    public Integer getBlessValue() {
        return blessValue;
    }

    public void setBlessValue(Integer blessValue) {
        this.blessValue = blessValue;
    }

    public Long getBlessClearTime() {
        return blessClearTime;
    }

    public void setBlessClearTime(Long blessClearTime) {
        this.blessClearTime = blessClearTime;
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

    public RoleXinmoMoshen copy() {
        RoleXinmoMoshen result = new RoleXinmoMoshen();
        result.setId(getId());
        result.setUserRoleId(getUserRoleId());
        result.setXmMoshenType(getXmMoshenType());
        result.setXmMoshenRank(getXmMoshenRank());
        result.setBlessValue(getBlessValue());
        result.setBlessClearTime(getBlessClearTime());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
