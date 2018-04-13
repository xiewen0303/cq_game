package com.junyou.bus.skill.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_skill_gewei")
public class RoleSkillGewei extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("skill_gewei")
	private Integer skillGewei;

	@Column("skill_id")
	private String skillId;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Long createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getSkillGewei(){
		return skillGewei;
	}

	public  void setSkillGewei(Integer skillGewei){
		this.skillGewei = skillGewei;
	}

	public String getSkillId(){
		return skillId;
	}

	public  void setSkillId(String skillId){
		this.skillId = skillId;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
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

	public RoleSkillGewei copy(){
		RoleSkillGewei result = new RoleSkillGewei();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSkillGewei(getSkillGewei());
		result.setSkillId(getSkillId());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
