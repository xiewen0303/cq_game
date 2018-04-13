package com.junyou.bus.xingkongbaozang.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refabu_xkbz")
public class RefabuXkbz extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("xf_gold")
	private Integer xfGold;
	
	@Column("xf_bgold")
	private Integer xfBgold;

	@Column("jifen")
	private Integer jifen;
	
	@Column("lingqu_status")
	private String lingquStatus;
	
	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Long createTime;

	@Column("sub_id")
	private Integer subId;


	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}
	
	
	
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

	public Integer getXfGold() {
		return xfGold;
	}

	public void setXfGold(Integer xfGold) {
		this.xfGold = xfGold;
	}

	public Integer getXfBgold() {
		return xfBgold;
	}

	public void setXfBgold(Integer xfBgold) {
		this.xfBgold = xfBgold;
	}

	public Integer getJifen() {
		return jifen;
	}

	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}

	public String getLingquStatus() {
		return lingquStatus;
	}

	public void setLingquStatus(String lingquStatus) {
		this.lingquStatus = lingquStatus;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public RefabuXkbz copy(){
		RefabuXkbz result = new RefabuXkbz();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setXfGold(getXfGold());
		result.setXfBgold(getXfBgold());
		result.setJifen(getJifen());
		result.setLingquStatus(getLingquStatus());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setSubId(getSubId());
		return result;
	}
}
