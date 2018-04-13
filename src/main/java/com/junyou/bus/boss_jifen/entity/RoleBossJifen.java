package com.junyou.bus.boss_jifen.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_boss_jifen")
public class RoleBossJifen extends AbsVersion implements Serializable,IEntity  {
	
	@EntityField
	private static final long serialVersionUID = 1L;
	
	@Column("user_role_id")
	private Long userRoleId;
	
	@Column("config_id")
	private Integer configId;
	
	@Column("jifen")
	private Long jifen;
	
	@Column("create_time")
	private Long createTime;
	
	@Column("update_time")
	private Long updateTime;
	

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
	public Long getJifen() {
		return jifen;
	}

	public void setJifen(Long jifen) {
		this.jifen = jifen;
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

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return this.userRoleId;
	}

	@Override
	public IEntity copy() {
		RoleBossJifen entity = new RoleBossJifen();
		entity.setUserRoleId(getUserRoleId());
		entity.setConfigId(getConfigId());
		entity.setCreateTime(getCreateTime());
		entity.setJifen(getJifen());
		entity.setUpdateTime(getUpdateTime());
		
		return entity;
	}
}
