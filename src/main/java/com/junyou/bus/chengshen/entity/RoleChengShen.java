package com.junyou.bus.chengshen.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_cheng_shen")
public class RoleChengShen extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("level")
	private Integer level;

	@Column("shenhun_value")
	private Long shenhunValue;

	@Column("update_level_time")
	private Long updateLevelTime;

	@Column("update_shenhun_time")
	private Long updateShenhunTime;

	@Column("create_time")
	private Timestamp createTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getLevel(){
		return level;
	}

	public  void setLevel(Integer level){
		this.level = level;
	}

	public Long getShenhunValue(){
		return shenhunValue;
	}

	public  void setShenhunValue(Long shenhunValue){
		this.shenhunValue = shenhunValue;
	}

	public Long getUpdateLevelTime(){
		return updateLevelTime;
	}

	public  void setUpdateLevelTime(Long updateLevelTime){
		this.updateLevelTime = updateLevelTime;
	}

	public Long getUpdateShenhunTime(){
		return updateShenhunTime;
	}

	public  void setUpdateShenhunTime(Long updateShenhunTime){
		this.updateShenhunTime = updateShenhunTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleChengShen copy(){
		RoleChengShen result = new RoleChengShen();
		result.setUserRoleId(getUserRoleId());
		result.setLevel(getLevel());
		result.setShenhunValue(getShenhunValue());
		result.setUpdateLevelTime(getUpdateLevelTime());
		result.setUpdateShenhunTime(getUpdateShenhunTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
