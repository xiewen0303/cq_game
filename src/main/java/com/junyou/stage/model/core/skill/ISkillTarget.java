/**
 * 
 */
package com.junyou.stage.model.core.skill;

import java.util.List;

import com.junyou.stage.model.core.element.IFighter;

/**
 * @description 技能目标
 * @author ShiJie Chi
 * @created 2011-12-6下午4:08:15
 */
public interface ISkillTarget {

	/**
	 * 目标是否在技能攻击范围内
	 * <br/>
	 * 根据  敌对 | 友方确认是否在技能施法范围内
	 * @param skill 
	 * @param
	 */
	public boolean inRange(ISkill skill, IFighter attacker, IFighter target);
	
	/**
	 * 施法者 选取要攻击的法宝阵目标
	 * @param skill
	 * @param fighter
	 * @param target
	 */
	public List<IFighter> chooseTargets(ISkill skill, IFighter fighter,IFighter target);
	

}
