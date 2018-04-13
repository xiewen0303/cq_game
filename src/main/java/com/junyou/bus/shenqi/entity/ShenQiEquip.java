package com.junyou.bus.shenqi.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("shen_qi_equip")
public class ShenQiEquip extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("shen_qi_id")
	private Integer shenQiId;

	@Column("slot")
	private Integer slot;
	

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



	public Integer getShenQiId() {
		return shenQiId;
	}



	public void setShenQiId(Integer shenQiId) {
		this.shenQiId = shenQiId;
	}



	public Integer getSlot() {
		return slot;
	}



	public void setSlot(Integer slot) {
		this.slot = slot;
	}

	public ShenQiEquip copy(){
		ShenQiEquip result = new ShenQiEquip();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setShenQiId(getShenQiId());
		result.setSlot(getSlot());
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
