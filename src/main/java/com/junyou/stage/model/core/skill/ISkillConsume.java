/**
 * 
 */
package com.junyou.stage.model.core.skill;

import com.junyou.stage.model.core.element.IFighter;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-6上午10:00:29
 */
public interface ISkillConsume {

	/**
	 * @param skill TODO
	 * @param
	 * @return 
	 */
	Object execute(IFighter attacker, ISkill skill);

	/**
	 * @param skill TODO
	 * @param
	 * @return 
	 */
	boolean isSatify(IFighter attacker, ISkill skill);

}
