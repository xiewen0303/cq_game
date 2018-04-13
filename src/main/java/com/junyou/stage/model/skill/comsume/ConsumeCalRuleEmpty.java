package com.junyou.stage.model.skill.comsume;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillConsume;

/**
 * 
 * @description 无消耗
 *
 * @author LiuJuan
 * @created 2011-12-13 下午04:07:09
 */
public class ConsumeCalRuleEmpty implements ISkillConsume {

	@Override
	public Object execute(IFighter attacker, ISkill skill) {
		return null;
	}

	@Override
	public boolean isSatify(IFighter attacker, ISkill skill) {
		return true;
	}

}
