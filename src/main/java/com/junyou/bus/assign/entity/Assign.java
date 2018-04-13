package com.junyou.bus.assign.entity;

import java.io.Serializable;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("assign")
public class Assign extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("assign_days")
	private Integer assignDays;

	@Column("assign_total")
	private Integer assignTotal;

	@Column("assign_all")
	private Integer assignAll;

	@Column("create_time")
	private Long createTime;

	@Column("update_time")
	private Long updateTime;

	@Column("assign_count")
	private Integer assignCount;

	@Column("retroactive_count")
	private Integer retroactiveCount;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getAssignDays() {
		return assignDays;
	}

	public void setAssignDays(Integer assignDays) {
		this.assignDays = assignDays;
	}

	public Integer getAssignTotal() {
		return assignTotal;
	}

	public void setAssignTotal(Integer assignTotal) {
		this.assignTotal = assignTotal;
	}

	public Integer getAssignAll() {
		return assignAll;
	}

	public void setAssignAll(Integer assignAll) {
		this.assignAll = assignAll;
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

	public Integer getAssignCount() {
		return assignCount;
	}

	public void setAssignCount(Integer assignCount) {
		this.assignCount = assignCount;
	}

	public Integer getRetroactiveCount() {
		return retroactiveCount;
	}

	public void setRetroactiveCount(Integer retroactiveCount) {
		this.retroactiveCount = retroactiveCount;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Assign copy() {
		Assign result = new Assign();
		result.setUserRoleId(getUserRoleId());
		result.setAssignDays(getAssignDays());
		result.setAssignTotal(getAssignTotal());
		result.setAssignAll(getAssignAll());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setAssignCount(getAssignCount());
		result.setRetroactiveCount(getRetroactiveCount());
		return result;
	}
}
