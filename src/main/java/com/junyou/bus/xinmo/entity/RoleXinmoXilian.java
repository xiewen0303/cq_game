package com.junyou.bus.xinmo.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xinmo_xilian")
public class RoleXinmoXilian extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("id")
    private Long id;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("xinmo_type")
    private Integer xinmoType;

    @Column("base_attr1")
    private String baseAttr1;

    @Column("base_attr2")
    private String baseAttr2;

    @Column("base_attr3")
    private String baseAttr3;

    @Column("back_attr1")
    private String backAttr1;

    @Column("back_attr2")
    private String backAttr2;

    @Column("back_attr3")
    private String backAttr3;

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

    public Integer getXinmoType() {
        return xinmoType;
    }

    public void setXinmoType(Integer xinmoType) {
        this.xinmoType = xinmoType;
    }

    public String getBaseAttr1() {
        return baseAttr1;
    }

    public void setBaseAttr1(String baseAttr1) {
        this.baseAttr1 = baseAttr1;
    }

    public String getBaseAttr2() {
        return baseAttr2;
    }

    public void setBaseAttr2(String baseAttr2) {
        this.baseAttr2 = baseAttr2;
    }

    public String getBaseAttr3() {
        return baseAttr3;
    }

    public void setBaseAttr3(String baseAttr3) {
        this.baseAttr3 = baseAttr3;
    }

    public String getBackAttr1() {
        return backAttr1;
    }

    public void setBackAttr1(String backAttr1) {
        this.backAttr1 = backAttr1;
    }

    public String getBackAttr2() {
        return backAttr2;
    }

    public void setBackAttr2(String backAttr2) {
        this.backAttr2 = backAttr2;
    }

    public String getBackAttr3() {
        return backAttr3;
    }

    public void setBackAttr3(String backAttr3) {
        this.backAttr3 = backAttr3;
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

    public RoleXinmoXilian copy() {
        RoleXinmoXilian result = new RoleXinmoXilian();
        result.setId(getId());
        result.setUserRoleId(getUserRoleId());
        result.setXinmoType(getXinmoType());
        result.setBaseAttr1(getBaseAttr1());
        result.setBaseAttr2(getBaseAttr2());
        result.setBaseAttr3(getBaseAttr3());
        result.setBackAttr1(getBackAttr1());
        result.setBackAttr2(getBackAttr2());
        result.setBackAttr3(getBackAttr3());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
