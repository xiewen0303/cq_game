package com.junyou.bus.fuben.entity;

import java.io.Serializable;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("xinmo_fuben")
public class XinmoFuben extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("fuhua_val")
    private Integer fuhuaVal;

    @Column("revert_time")
    private Long revertTime;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    @EntityField
    private int fuhuaRunnableState = GameConstants.XM_FUHUA_RUNNABLE_STATE_OVER;
    
    @EntityField
    private int state = GameConstants.FUBEN_STATE_READY;

    public int getState() {
        return state;
    }

    public int getFuhuaRunnableState() {
        return fuhuaRunnableState;
    }

    public void setFuhuaRunnableState(int fuhuaRunnableState) {
        this.fuhuaRunnableState = fuhuaRunnableState;
    }

    public void setState(int state) {
        this.state = state;
    }

    
    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getFuhuaVal() {
        return fuhuaVal;
    }

    public void setFuhuaVal(Integer fuhuaVal) {
        this.fuhuaVal = fuhuaVal;
    }

    public Long getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(Long revertTime) {
        this.revertTime = revertTime;
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

    public XinmoFuben copy() {
        XinmoFuben result = new XinmoFuben();
        result.setUserRoleId(getUserRoleId());
        result.setFuhuaVal(getFuhuaVal());
        result.setRevertTime(getRevertTime());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
