package com.junyou.bus.oncechong.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_oncechong")
public class RoleOncechong extends AbsVersion implements Serializable, IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;
	
	@Column("user_role_id")
	private Long userRoleId;

	@Column("config_id")
	private Integer configId;

	@Column("chong_count")
	private Integer chongCount;
	
	@Column("receive_count")
	private Integer receiveCount;

	@Column("update_time")
	private Long updateTime;

	@Column("sub_id")
	private Integer subId;
	
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
	
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public Integer getChongCount() {
		return chongCount;
	}
	public void setChongCount(Integer chongCount) {
		this.chongCount = chongCount;
	}
	public Integer getReceiveCount() {
		return receiveCount;
	}
	public void setReceiveCount(Integer receiveCount) {
		this.receiveCount = receiveCount;
	}
	public Long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getSubId() {
		return subId;
	}
	public void setSubId(Integer subId) {
		this.subId = subId;
	}
 
	public RoleOncechong copy() {
		RoleOncechong result = new RoleOncechong();
		result.setReceiveCount(getReceiveCount());
		result.setChongCount(getChongCount());
		result.setId(getId());
		result.setSubId(getSubId());
		result.setConfigId(getConfigId());
		result.setUpdateTime(getUpdateTime());
		result.setUserRoleId(getUserRoleId());
		return result;
	}
	
	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}
}
