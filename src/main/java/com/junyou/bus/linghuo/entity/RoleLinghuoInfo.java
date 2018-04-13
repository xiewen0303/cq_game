package com.junyou.bus.linghuo.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_linghuo_info")
public class RoleLinghuoInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("linghuo_id")
	private Integer linghuoId;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getLinghuoId(){
		return linghuoId;
	}

	public  void setLinghuoId(Integer linghuoId){
		this.linghuoId = linghuoId;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
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

	public RoleLinghuoInfo copy(){
		RoleLinghuoInfo result = new RoleLinghuoInfo();
		result.setUserRoleId(getUserRoleId());
		result.setLinghuoId(getLinghuoId());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
