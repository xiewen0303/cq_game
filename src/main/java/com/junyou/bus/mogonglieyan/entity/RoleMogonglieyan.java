package com.junyou.bus.mogonglieyan.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_mogonglieyan")
public class RoleMogonglieyan extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("yumo_val")
    private Integer yumoVal;

    @Column("jinghua_val")
    private Long jinghuaVal;

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

    public Integer getYumoVal() {
        return yumoVal;
    }

    public void setYumoVal(Integer yumoVal) {
        this.yumoVal = yumoVal;
    }

    public Long getJinghuaVal() {
        return jinghuaVal;
    }

    public void setJinghuaVal(Long jinghuaVal) {
        this.jinghuaVal = jinghuaVal;
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

    public RoleMogonglieyan copy() {
        RoleMogonglieyan result = new RoleMogonglieyan();
        result.setUserRoleId(getUserRoleId());
        result.setYumoVal(getYumoVal());
        result.setJinghuaVal(getJinghuaVal());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
