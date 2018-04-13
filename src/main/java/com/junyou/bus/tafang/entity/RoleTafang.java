package com.junyou.bus.tafang.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_tafang")
public class RoleTafang extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("exp")
	private Long exp;

	@Column("join_time")
	private Integer joinTime;
	
	@Column("cal")
	private Integer cal;

	@Column("update_time")
	private Long updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Long getExp(){
		return exp;
	}

	public  void setExp(Long exp){
		this.exp = exp;
	}

	public Integer getJoinTime(){
		return joinTime;
	}

	public  void setJoinTime(Integer joinTime){
		this.joinTime = joinTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}
	

	public Integer getCal() {
		return cal;
	}

	public void setCal(Integer cal) {
		this.cal = cal;
	}

	public RoleTafang copy(){
		RoleTafang result = new RoleTafang();
		result.setUserRoleId(getUserRoleId());
		result.setExp(getExp());
		result.setCal(getCal());
		result.setJoinTime(getJoinTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
