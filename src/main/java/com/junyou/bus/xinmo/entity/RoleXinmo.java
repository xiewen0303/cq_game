package com.junyou.bus.xinmo.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xinmo")
public class RoleXinmo extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("xinmo_id")
    private Integer xinmoId;

    @Column("xinmo_exp")
    private Integer xinmoExp;

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

    public Integer getXinmoId() {
        return xinmoId;
    }

    public void setXinmoId(Integer xinmoId) {
        this.xinmoId = xinmoId;
    }

    public Integer getXinmoExp() {
        return xinmoExp;
    }

    public void setXinmoExp(Integer xinmoExp) {
        this.xinmoExp = xinmoExp;
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

    public RoleXinmo copy() {
        RoleXinmo result = new RoleXinmo();
        result.setUserRoleId(getUserRoleId());
        result.setXinmoId(getXinmoId());
        result.setXinmoExp(getXinmoExp());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
