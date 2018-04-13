/**
 * 
 */
package com.junyou.stage.model.skill;

import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.configure.export.impl.SkillConfig;
import com.junyou.stage.configure.export.impl.SkillXueXiConfig;
import com.junyou.stage.model.core.fight.IFightResultCalculator;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillConsume;
import com.junyou.stage.model.fight.calculator.FightResultCalculatorFactory;
import com.junyou.stage.model.fight.calculator.FightResultCalculatorType;
import com.junyou.stage.model.skill.comsume.ConsumeCalRuleFactory;
import com.junyou.stage.model.skill.comsume.SkillConsumes;
import com.junyou.stage.model.skill.firepath.SkillFirePathFactory;
import com.junyou.stage.model.skill.hatred.DefaultSkillHatred;
import com.junyou.stage.model.skill.hatred.ISkillHatred;
import com.junyou.stage.model.skill.target.TargetFactory;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-13上午10:31:40
 */
public class SkillFactory {

	/**
	 * @param
	 */
	public ISkill create(String skillCategory, Integer level) {
		
		SkillConfig skillConfig = StageConfigureHelper.getSkillConfigExportService().loadById(skillCategory);
		if(skillConfig == null){
			ChuanQiLog.error("skillConfig is null skillId"+skillCategory);
			return null;
		}
		SkillXueXiConfig config = skillConfig.getXuexiConfig(level);
		if(config == null){
			ChuanQiLog.error("SkillXueXiConfig is null skillId "+skillCategory+",level " + level);
			return null;
		}
		Skill skill = new Skill(skillConfig, level);
		
		//其他一些参数
		
		//设置消耗类型
		ISkillConsume skillConsume = ConsumeCalRuleFactory.getConsumeCalRule(SkillConsumes.CONSUME_TYPE_EMPTY);
		skill.setSkillConsume(skillConsume);
		
//		//设置目标类型
		skill.setSkillTarget(TargetFactory.getSkillTarget(skillConfig.getTargetType()));
		//设置技能施法路径
		skill.setSkillFirePath(SkillFirePathFactory.getSkillFirePath(skillConfig.getSkillFirePathType()));
		
		//设置战斗计算公式
		FightResultCalculatorType damageType = FightResultCalculatorType.convert(skillConfig.getGongshi());
		IFightResultCalculator fightResultCalculator = FightResultCalculatorFactory.getFightResultCalculator(damageType);
		skill.setFightResultCalculator(fightResultCalculator);
		skill.setDamageType(damageType);
		
		//设置仇恨计算公式
		ISkillHatred skillHatred = new DefaultSkillHatred(skillConfig);
		skill.setSkillHatred(skillHatred);
		
		
		return skill;
	}
	

}
