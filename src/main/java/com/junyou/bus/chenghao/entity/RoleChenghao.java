package com.junyou.bus.chenghao.entity;

import java.io.Serializable;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_chenghao")
public class RoleChenghao extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("chenghao_id")
	private Integer chenghaoId;

	@Column("name")
	private String name;

	@Column("res")
	private String res;

	@Column("wear_status")
	private Integer wearStatus;

	@Column("expire_time")
	private Long expireTime;

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

	public Integer getChenghaoId() {
		return chenghaoId;
	}

	public void setChenghaoId(Integer chenghaoId) {
		this.chenghaoId = chenghaoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public Integer getWearStatus() {
		return wearStatus;
	}

	public void setWearStatus(Integer wearStatus) {
		this.wearStatus = wearStatus;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
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

	public RoleChenghao copy() {
		RoleChenghao result = new RoleChenghao();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setChenghaoId(getChenghaoId());
		result.setName(getName());
		result.setRes(getRes());
		result.setWearStatus(getWearStatus());
		result.setExpireTime(getExpireTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
