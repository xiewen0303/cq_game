package com.junyou.bus.equip.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xiangwei")
public class RoleXiangwei extends AbsVersion implements Serializable,IEntity {
	
	@EntityField
	private static final long serialVersionUID = 1L;
	
	@Column("id")
	private Long id;
	
	@Column("user_role_id")
	private Long userRoleId;
	
	@Column("buwei")
	private Integer buwei;
	
	@Column("xiangwei_id")
	private Integer xiangweiId;
	
	@Column("xiangwei_star")	
	private Integer xiangweiStar;
	
	@Column("update_time")		
	private Long updateTime;
	

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
	public Integer getBuwei() {
		return buwei;
	}

	public void setBuwei(Integer buwei) {
		this.buwei = buwei;
	}
	public Integer getXiangweiId() {
		return xiangweiId;
	}

	public void setXiangweiId(Integer xiangweiId) {
		this.xiangweiId = xiangweiId;
	}
	public Integer getXiangweiStar() {
		return xiangweiStar;
	}

	public void setXiangweiStar(Integer xiangweiStar) {
		this.xiangweiStar = xiangweiStar;
	}
	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
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

	@Override
	public IEntity copy() {
		RoleXiangwei entity = new RoleXiangwei();
		entity.setId(getId());
		entity.setBuwei(getBuwei());
		entity.setUpdateTime(getUpdateTime());
		entity.setUserRoleId(getUserRoleId());
		entity.setXiangweiId(getXiangweiId());
		entity.setXiangweiStar(getXiangweiStar());
		return entity;
	}
}
