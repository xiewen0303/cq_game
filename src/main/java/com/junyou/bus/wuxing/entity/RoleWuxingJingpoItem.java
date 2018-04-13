package com.junyou.bus.wuxing.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_wuxing_jingpo_item")
public class RoleWuxingJingpoItem extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("guid")
    private Long guid;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("goods_id")
    private Integer goodsId;

    @Column("position")
    private Integer position;

    @Column("slot")
    private Integer slot;

    @Column("eat_exp")
    private Integer eatExp;

    @Column("create_time")
    private Long createTime;

    @Column("update_time")
    private Long updateTime;

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Integer getEatExp() {
        return eatExp;
    }

    public void setEatExp(Integer eatExp) {
        this.eatExp = eatExp;
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
        return "guid";
    }

    @Override
    public Long getPrimaryKeyValue() {
        return guid;
    }

    public RoleWuxingJingpoItem copy() {
        RoleWuxingJingpoItem result = new RoleWuxingJingpoItem();
        result.setGuid(getGuid());
        result.setUserRoleId(getUserRoleId());
        result.setGoodsId(getGoodsId());
        result.setPosition(getPosition());
        result.setSlot(getSlot());
        result.setEatExp(getEatExp());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
