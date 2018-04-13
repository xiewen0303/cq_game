package com.junyou.bus.fuben.entity;

import java.io.Serializable;

import com.junyou.constants.GameConstants;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("wuxing_skill_fuben")
public class WuxingSkillFuben extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("user_role_Id")
    private Long userRoleId;

    @Column("pass_layer")
    private Integer passLayer;

    @Column("clear_layer")
    private Integer clearLayer;

    @Column("can_clear_layer")
    private Integer canClearLayer;

    @Column("add_buff")
    private Integer addBuff;

    @Column("sub_buff")
    private Integer subBuff;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    @EntityField
    private int state = GameConstants.FUBEN_STATE_READY;

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getPassLayer() {
        return passLayer;
    }

    public void setPassLayer(Integer passLayer) {
        this.passLayer = passLayer;
    }

    public Integer getClearLayer() {
        return clearLayer;
    }

    public void setClearLayer(Integer clearLayer) {
        this.clearLayer = clearLayer;
    }

    public Integer getCanClearLayer() {
        return canClearLayer;
    }

    public void setCanClearLayer(Integer canClearLayer) {
        this.canClearLayer = canClearLayer;
    }

    public Integer getAddBuff() {
        return addBuff;
    }

    public void setAddBuff(Integer addBuff) {
        this.addBuff = addBuff;
    }

    public Integer getSubBuff() {
        return subBuff;
    }

    public void setSubBuff(Integer subBuff) {
        this.subBuff = subBuff;
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

    public WuxingSkillFuben copy() {
        WuxingSkillFuben result = new WuxingSkillFuben();
        result.setUserRoleId(getUserRoleId());
        result.setPassLayer(getPassLayer());
        result.setClearLayer(getClearLayer());
        result.setCanClearLayer(getCanClearLayer());
        result.setAddBuff(getAddBuff());
        result.setSubBuff(getSubBuff());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
