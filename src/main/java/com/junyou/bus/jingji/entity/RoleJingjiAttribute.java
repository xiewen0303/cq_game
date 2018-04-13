package com.junyou.bus.jingji.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_jingji_attribute")
public class RoleJingjiAttribute extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("attribute")
	private byte[] attribute;
	
	@Column("attribute2")
	private byte[] attribute2;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public byte[] getAttribute(){
		return attribute;
	}

	public  void setAttribute(byte[] attribute){
		this.attribute = attribute;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public byte[] getAttribute2() {
		return attribute2;
	}

	public void setAttribute2(byte[] attribute2) {
		this.attribute2 = attribute2;
	}

	public RoleJingjiAttribute copy(){
		RoleJingjiAttribute result = new RoleJingjiAttribute();
		result.setUserRoleId(getUserRoleId());
		result.setAttribute(getAttribute());
		result.setAttribute2(getAttribute2());
		return result;
	}
}
