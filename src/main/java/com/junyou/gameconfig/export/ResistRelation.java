package com.junyou.gameconfig.export;


/**
 * 
 * @description 抵抗技能关系
 *
 * @author LiuJuan
 * @date 2012-3-9 下午6:24:12
 */
public class ResistRelation {

	private String primary;
	private String resistSkillId;
	private String skillType;
	
	/**
	 * 技能大类编号
	 * @return
	 */
	public String getSkillType() {
		return skillType;
	}
	public void setSkillType(String skillType) {
		this.skillType = skillType;
	}
	/**
	 * 抵抗技能编号
	 * @return
	 */
	public String getResistSkillId() {
		return resistSkillId;
	}
	public void setResistSkillId(String resistSkillId) {
		this.resistSkillId = resistSkillId;
		this.primary = resistSkillId;
	}

	public String getPirmaryKeyName() {
		
		return "resistSkillId";
	}

	public String getPrimaryKeyValue() {
		
		return primary;
	}
	
	public ResistRelation copy() {
		ResistRelation relation = new ResistRelation();
		relation.setSkillType(this.getSkillType());
		relation.setResistSkillId(this.getResistSkillId());
		
		return relation;
	}
	
}
