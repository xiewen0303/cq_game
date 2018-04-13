/**
 * 
 */
package com.junyou.stage.model.core.fight;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;

/**
 * @description 战斗结果计算器
 * @author ShiJie Chi
 * @created 2011-12-9上午10:46:43
 */
public interface IFightResultCalculator {

	/**
	 * 计算
	 * @param
	 * @return 
	 */
	public IFightResult calculate(ISkill skill, IFighter attacker, IFighter target);

}
