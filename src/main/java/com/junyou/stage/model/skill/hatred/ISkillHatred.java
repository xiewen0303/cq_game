package com.junyou.stage.model.skill.hatred;


public interface ISkillHatred {

	/**
	 * 根据伤害计算仇恨-此处配置攻击者使用该技能对被攻击者产生的仇恨
	 */
	Integer cal1(long harmVal);

	/**
	 * 根据伤害计算仇恨-此处配置被该技能攻击后，被攻击者对攻击者产生的仇恨
	 * @param harmVal
	 * @return
	 */
	Integer cal2(long harmVal);
}
