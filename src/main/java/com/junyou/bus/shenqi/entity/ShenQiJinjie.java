package com.junyou.bus.shenqi.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("shen_qi_jinjie")
public class ShenQiJinjie extends AbsVersion implements Serializable,IEntity{
	
	@EntityField
	private static final long serialVersionUID = 1L;
	
	@Column("id")
	private Long id;
	
	@Column("user_role_id")
	private Long userRoleId;
	
	@Column("shen_qi_id")
	private Integer shenQiId;
	
	@Column("zfz_val")
	private Integer zfzVal;
	
	@Column("shenqi_level")
	private Integer shenqiLevel;
	
	@Column("last_sj_time")
	private Long lastSjTime;
	
	@Column("update_time")
	private Timestamp updateTime;
	

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
	public Integer getShenQiId() {
		return shenQiId;
	}

	public void setShenQiId(Integer shenQiId) {
		this.shenQiId = shenQiId;
	}
	public Integer getZfzVal() {
		return zfzVal;
	}

	public void setZfzVal(Integer zfzVal) {
		this.zfzVal = zfzVal;
	}
	public Integer getShenqiLevel() {
		return shenqiLevel;
	}

	public void setShenqiLevel(Integer shenqiLevel) {
		this.shenqiLevel = shenqiLevel;
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
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	@Override
	public IEntity copy() {
		ShenQiJinjie entity = new ShenQiJinjie();
		entity.setId(id);
		entity.setLastSjTime(lastSjTime);
		entity.setShenQiId(shenQiId);
		entity.setShenqiLevel(shenqiLevel);
		entity.setUpdateTime(updateTime);
		entity.setUserRoleId(userRoleId);
		entity.setZfzVal(zfzVal);
		return entity;
	}
}
