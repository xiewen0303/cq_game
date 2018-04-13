package com.junyou.bus.xinmo.entity;

import java.io.Serializable;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xinmo_liandan")
public class RoleXinmoLiandan extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("liandan_id")
    private Integer liandanId;

    @Column("open_solt")
    private Integer openSolt;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;
    
    @EntityField
    private int lianDanRunnableState = GameConstants.XM_LIANDAN_RUNNABLE_STATE_OVER;

    public int getLianDanRunnableState() {
        return lianDanRunnableState;
    }

    public void setLianDanRunnableState(int lianDanRunnableState) {
        this.lianDanRunnableState = lianDanRunnableState;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getLiandanId() {
        return liandanId;
    }

    public void setLiandanId(Integer liandanId) {
        this.liandanId = liandanId;
    }

    public Integer getOpenSolt() {
        return openSolt;
    }

    public void setOpenSolt(Integer openSolt) {
        this.openSolt = openSolt;
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

    public RoleXinmoLiandan copy() {
        RoleXinmoLiandan result = new RoleXinmoLiandan();
        result.setUserRoleId(getUserRoleId());
        result.setLiandanId(getLiandanId());
        result.setOpenSolt(getOpenSolt());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
