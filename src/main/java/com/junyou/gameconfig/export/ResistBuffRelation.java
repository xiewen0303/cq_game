package com.junyou.gameconfig.export;


/**
 * 
 * @description BUFF效果关系 
 *
 * @author LiuJuan
 * @date 2012-3-9 下午6:20:03
 */
public class ResistBuffRelation {

	private String buffType;
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
	 * 效果类型编号
	 * @return
	 */
	public String getBuffType() {
		return buffType;
	}
	public void setBuffType(String buffType) {
		this.buffType = buffType;
	}
	
	public String getPirmaryKeyName() {
		
		return "buffType";
	}
	public String getPrimaryKeyValue() {
		
		return buffType;
	}
	
	public ResistBuffRelation copy() {
		ResistBuffRelation relation = new ResistBuffRelation();
		relation.setSkillType(this.skillType);
		relation.setBuffType(this.buffType);
		
		return relation;
	}
}
