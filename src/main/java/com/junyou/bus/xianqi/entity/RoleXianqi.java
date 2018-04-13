package com.junyou.bus.xianqi.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_xianqi")
public class RoleXianqi extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("xiandong_lvl")
	private Integer xiandongLvl;

	@Column("xiandong_exp")
	private Long xiandongExp;

	@Column("create_time")
	private Long createTime;

	@Column("update_time")
	private Long updateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getXiandongLvl(){
		return xiandongLvl;
	}

	public  void setXiandongLvl(Integer xiandongLvl){
		this.xiandongLvl = xiandongLvl;
	}

	public Long getXiandongExp(){
		return xiandongExp;
	}

	public  void setXiandongExp(Long xiandongExp){
		this.xiandongExp = xiandongExp;
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

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleXianqi copy(){
		RoleXianqi result = new RoleXianqi();
		result.setUserRoleId(getUserRoleId());
		result.setXiandongLvl(getXiandongLvl());
		result.setXiandongExp(getXiandongExp());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
