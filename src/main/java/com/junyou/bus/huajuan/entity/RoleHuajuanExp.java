package com.junyou.bus.huajuan.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_huajuan_exp")
public class RoleHuajuanExp extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("exp")
	private Integer exp;

	@Column("create_time")
	private Long createTime;

	@Column("update_time")
	private Long updateTime;

	@Column("kucun_exp")
	private Integer kucunExp;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getExp(){
		return exp;
	}

	public  void setExp(Integer exp){
		this.exp = exp;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getKucunExp(){
		return kucunExp;
	}

	public  void setKucunExp(Integer kucunExp){
		this.kucunExp = kucunExp;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleHuajuanExp copy(){
		RoleHuajuanExp result = new RoleHuajuanExp();
		result.setUserRoleId(getUserRoleId());
		result.setExp(getExp());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		result.setKucunExp(getKucunExp());
		return result;
	}
}
