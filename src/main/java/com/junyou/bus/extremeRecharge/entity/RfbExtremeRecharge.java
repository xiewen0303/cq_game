package com.junyou.bus.extremeRecharge.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("rfb_extreme_recharge")
public class RfbExtremeRecharge  extends AbsVersion implements Serializable, IEntity  {
	
	@EntityField
	private static final long serialVersionUID = 1L;
	
	@Column("id")
	private Long id;
	
	@Column("user_role_id")
	private Long userRoleId;
	
	@Column("count")
	private Integer count;
	
	@Column("sub_id")
	private Integer subId;
	
	@Column("update_time")
	private Long updateTime;
	
	@Column("create_time")
	private Timestamp createTime;
	

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
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}
	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
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

	@Override
	public IEntity copy() {
		RfbExtremeRecharge entity = new RfbExtremeRecharge();
		entity.setId(getId());
		entity.setCreateTime(getCreateTime());
		entity.setSubId(getSubId());
		entity.setUpdateTime(getUpdateTime());
		entity.setUserRoleId(getUserRoleId());
		entity.setCount(getCount());
		return entity;
	}
}
