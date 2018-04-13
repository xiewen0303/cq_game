package com.junyou.bus.xinmo.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xinmo_liandan_item")
public class RoleXinmoLiandanItem extends AbsVersion implements Serializable, IEntity {

    @EntityField
    private static final long serialVersionUID = 1L;

    @Column("guid")
    private Long guid;

    @Column("user_role_id")
    private Long userRoleId;

    @Column("solt")
    private Integer solt;

    @Column("goods_id")
    private String goodsId;

    @Column("goods_num")
    private Integer goodsNum;

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

    public Integer getSolt() {
        return solt;
    }

    public void setSolt(Integer solt) {
        this.solt = solt;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
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

    public RoleXinmoLiandanItem copy() {
        RoleXinmoLiandanItem result = new RoleXinmoLiandanItem();
        result.setGuid(getGuid());
        result.setUserRoleId(getUserRoleId());
        result.setSolt(getSolt());
        result.setGoodsId(getGoodsId());
        result.setGoodsNum(getGoodsNum());
        result.setCreateTime(getCreateTime());
        result.setUpdateTime(getUpdateTime());
        return result;
    }
}
