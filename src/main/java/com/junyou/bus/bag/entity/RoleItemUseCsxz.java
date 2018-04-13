package com.junyou.bus.bag.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Primary;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

/**
 * 物品每日限制使用次数
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午4:04:30
  *@Description:
 */
@Table("role_item_use_csxz")
public class RoleItemUseCsxz extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Primary
	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("xz_id")
	private Integer xzId;

	@Column("use_count")
	private Integer useCount;

	@Column("last_use_time")
	private Long lastUseTime;

	@Column("create_time")
	private Long createTime;
	

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
	
	public Integer getXzId() {
		return xzId;
	}

	public void setXzId(Integer xzId) {
		this.xzId = xzId;
	}

	public Integer getUseCount() {
		return useCount;
	}

	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}
	public Long getLastUseTime() {
		return lastUseTime;
	}

	public void setLastUseTime(Long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	@Override	
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return getId();
	}
	
	public RoleItemUseCsxz copy(){
		RoleItemUseCsxz result = new RoleItemUseCsxz();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setXzId(getXzId());
		result.setUseCount(getUseCount());
		result.setLastUseTime(getLastUseTime());
		result.setCreateTime(getCreateTime());
		return result;
	}


}
