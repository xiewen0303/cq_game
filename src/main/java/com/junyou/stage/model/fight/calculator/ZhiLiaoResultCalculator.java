/**
 * 
 */
package com.junyou.stage.model.fight.calculator;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.skill.harm.Harm;
import com.junyou.stage.model.skill.harm.HarmType;

/**
 * 治疗
 * @author LiuYu
 * @date 2015-2-2 下午1:55:44
 */
public class ZhiLiaoResultCalculator extends AbsFightResultCalculator implements IFightResultCalculator {

	@Override
	public IHarm harmCalculate(ISkill skill, IFighter attacker, IFighter target)  {
		
		float c = skill.getSkillXueXiConfig().getFormulec();
		
		long hp = target.getFightAttribute().getMaxHp();
		
		Float damage = hp * c / EffectType.getAttBase();
		damage += skill.getSkillXueXiConfig().getFormuleb();
		
		HarmType harmType = HarmType.ZHILIAO;
		
		IHarm harm = new Harm(skill, attacker,target, harmType, damage.longValue());
		return harm;
	}
	
}
