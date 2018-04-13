package com.junyou.bus.touzi.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_touzi")
public class RoleTouzi extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("config_id")
	private Integer configId;

	@Column("tz_type")
	private Integer tzType;
	
	@Column("recevie_day")
	private Integer recevieDay;

	@Column("create_time")
	private Long createTime;
	
	@Column("update_time")
	private Long updateTime;

	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Integer getTzType() {
		return tzType;
	}

	public void setTzType(Integer loginDay) {
		this.tzType = loginDay;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getRecevieDay() {
		return recevieDay;
	}

	public void setRecevieDay(Integer recevieDay) {
		this.recevieDay = recevieDay;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleTouzi copy(){
		RoleTouzi result = new RoleTouzi();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setConfigId(getConfigId());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setTzType(getTzType());
		result.setRecevieDay(getRecevieDay());
		return result;
	}
}
