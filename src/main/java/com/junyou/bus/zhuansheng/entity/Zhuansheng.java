package com.junyou.bus.zhuansheng.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("zhuansheng")
public class Zhuansheng extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sz_level")
	private Integer szLevel;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getSzLevel(){
		return szLevel;
	}

	public  void setSzLevel(Integer szLevel){
		this.szLevel = szLevel;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public Zhuansheng copy(){
		Zhuansheng result = new Zhuansheng();
		result.setUserRoleId(getUserRoleId());
		result.setSzLevel(getSzLevel());
		return result;
	}
}
