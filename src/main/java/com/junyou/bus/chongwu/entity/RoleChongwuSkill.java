package com.junyou.bus.chongwu.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_chongwu_skill")
public class RoleChongwuSkill extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("chongwu_id")
	private Integer chongwuId;

	@Column("skill_info1")
	private String skillInfo1;

	@Column("skill_info2")
	private String skillInfo2;

	@Column("skill_info3")
	private String skillInfo3;

	@Column("create_time")
	private Long createTime;

	@Column("update_time")
	private Long updateTime;


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

	public Integer getChongwuId(){
		return chongwuId;
	}

	public  void setChongwuId(Integer chongwuId){
		this.chongwuId = chongwuId;
	}

	public String getSkillInfo1(){
		return skillInfo1;
	}

	public  void setSkillInfo1(String skillInfo1){
		this.skillInfo1 = skillInfo1;
	}

	public String getSkillInfo2(){
		return skillInfo2;
	}

	public  void setSkillInfo2(String skillInfo2){
		this.skillInfo2 = skillInfo2;
	}

	public String getSkillInfo3(){
		return skillInfo3;
	}

	public  void setSkillInfo3(String skillInfo3){
		this.skillInfo3 = skillInfo3;
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
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleChongwuSkill copy(){
		RoleChongwuSkill result = new RoleChongwuSkill();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setChongwuId(getChongwuId());
		result.setSkillInfo1(getSkillInfo1());
		result.setSkillInfo2(getSkillInfo2());
		result.setSkillInfo3(getSkillInfo3());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
