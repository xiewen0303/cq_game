/**
 * 
 */
package com.junyou.stage.model.core.fight;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-5下午4:06:29
 */
public interface IFightResult {

	/**
	 * @param attacker TODO
	 * @param skill TODO
	 * @param
	 * @return 
	 */
	public void execute(IFighter target, IFighter attacker, ISkill skill);
	
	/**
	 * 是否miss
	 */
	public boolean isMiss();
	
	/**
	 * 
	 */
	public void reboundExecute(IFighter target, IFighter attacker, ISkill skill);

	/**
	 * 获取伤害
	 */
	public IHarm getHarm();
	
}
