package com.junyou.bus.huajuan2.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_huajuan2")
public class RoleHuajuan2 extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("group_id")
	private Integer groupId;

	@Column("config_id")
	private Integer configId;

	@Column("is_up")
	private Integer isUp;

	@Column("star")
	private Integer star;

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

	public Integer getGroupId(){
		return groupId;
	}

	public  void setGroupId(Integer groupId){
		this.groupId = groupId;
	}

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Integer getIsUp(){
		return isUp;
	}

	public  void setIsUp(Integer isUp){
		this.isUp = isUp;
	}

	public Integer getStar(){
		return star;
	}

	public  void setStar(Integer star){
		this.star = star;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
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

	public RoleHuajuan2 copy(){
		RoleHuajuan2 result = new RoleHuajuan2();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setGroupId(getGroupId());
		result.setConfigId(getConfigId());
		result.setIsUp(getIsUp());
		result.setStar(getStar());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
