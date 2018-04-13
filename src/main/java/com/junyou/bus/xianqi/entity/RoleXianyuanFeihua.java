package com.junyou.bus.xianqi.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xianyuan_feihua")
public class RoleXianyuanFeihua extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("feihua_level")
	private Integer feihuaLevel;

	@Column("zfz_val")
	private Integer zfzVal;

	@Column("last_sj_time")
	private Long lastSjTime;

	@Column("update_time")
	private Timestamp updateTime;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getFeihuaLevel() {
		return feihuaLevel;
	}

	public void setFeihuaLevel(Integer feihuaLevel) {
		this.feihuaLevel = feihuaLevel;
	}

	public Integer getZfzVal() {
		return zfzVal;
	}

	public void setZfzVal(Integer zfzVal) {
		this.zfzVal = zfzVal;
	}

	public Long getLastSjTime() {
		return lastSjTime;
	}

	public void setLastSjTime(Long lastSjTime) {
		this.lastSjTime = lastSjTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
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

	@Override
	public RoleXianyuanFeihua copy() {
		RoleXianyuanFeihua entity = new RoleXianyuanFeihua();
		entity.setUserRoleId(getUserRoleId());
		entity.setFeihuaLevel(getFeihuaLevel());
		entity.setLastSjTime(getLastSjTime());
		entity.setUpdateTime(getUpdateTime());
		entity.setZfzVal(getZfzVal());
		return entity;
	}
}
