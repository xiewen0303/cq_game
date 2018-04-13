package com.junyou.bus.shizhuang.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_shizhuang_jinjie")
public class RoleShiZhuangJinJie extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("shizhuang_id")
	private Integer shizhuangId;

	@Column("jie_level")
	private Integer jieLevel;
	
	@Column("update_time")
	private Long updateTime;//过期时间

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

	public Integer getShizhuangId() {
		return shizhuangId;
	}

	public void setShizhuangId(Integer shizhuangId) {
		this.shizhuangId = shizhuangId;
	}

	
	public Integer getJieLevel() {
		return jieLevel;
	}

	public void setJieLevel(Integer jieLevel) {
		this.jieLevel = jieLevel;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public RoleShiZhuangJinJie copy(){
		RoleShiZhuangJinJie result = new RoleShiZhuangJinJie();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setShizhuangId(getShizhuangId());
		result.setJieLevel(getJieLevel());
		result.setUpdateTime(getUpdateTime());
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
