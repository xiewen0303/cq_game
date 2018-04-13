package com.junyou.bus.huanhua.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("role_huanhua")
public class RoleHuanhua extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("type")
	private Integer type;

	@Column("config_id")
	private Integer configId;

	@Column("create_time")
	private Long createTime;


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

	public Integer getType(){
		return type;
	}

	public  void setType(Integer type){
		this.type = type;
	}

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleHuanhua copy(){
		RoleHuanhua result = new RoleHuanhua();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setType(getType());
		result.setConfigId(getConfigId());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
