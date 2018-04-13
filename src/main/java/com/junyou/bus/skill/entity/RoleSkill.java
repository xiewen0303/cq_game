package com.junyou.bus.skill.entity;
import java.io.Serializable;

import com.junyou.bus.skill.utils.RoleSkillWarpper;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_skill")
public class RoleSkill extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("skill_id")
	private String skillId;

	@Column("level")
	private Integer level;

	@Column("shulian")
	private Integer shulian;
	
	@Column("type")
	private Integer type;
	
	@EntityField
	private Object[] info;
	
	@EntityField
	private int maxShulian;


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

	public String getSkillId(){
		return skillId;
	}

	public  void setSkillId(String skillId){
		this.skillId = skillId;
	}

	public Integer getLevel(){
		return level;
	}

	public  void setLevel(Integer level){
		this.level = level;
		if(info != null){
			info[2] = level;
		}
	}

	public Integer getShulian(){
		return shulian;
	}

	public  void setShulian(Integer shulian){
		this.shulian = shulian;
	}
	
	public void addShulian(int value){
		this.shulian += value;
	}
	
	public void clearShulian(){
		this.shulian = 0;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RoleSkill copy(){
		RoleSkill result = new RoleSkill();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSkillId(getSkillId());
		result.setLevel(getLevel());
		result.setShulian(getShulian());
		result.setType(getType());
		return result;
	}
	
	public int getMaxShulian() {
		return maxShulian;
	}

	public void setMaxShulian(int maxShulian) {
		this.maxShulian = maxShulian;
	}

	public Object[] getInfo(){
		if(info == null){
			info = RoleSkillWarpper.getSkillInfo(this);
		}else{
			info[3] = shulian;
		}
		return info;
	}
}
