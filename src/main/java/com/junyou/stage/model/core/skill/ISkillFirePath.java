package com.junyou.stage.model.core.skill;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;

/**
 * @description 技能施法路径
 * @author ShiJie Chi
 * @date 2012-6-8 下午1:20:38 
 */
public interface ISkillFirePath {
	
	/**
	 * 是否在路径内
	 * @param skill 施法技能
	 * @param attacker 攻击方
	 * @param target 目标方
	 * @param helpParams 路径验证帮助参数
	 * @param draft 草稿，记载本次计算的中间数值，可供下次相同计算使用
	 */
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParam,IStage stage,ConfirmDraft draft);

	/**
	 * 生成路径辅助参数
	 * @description
	 */
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target);
	
}
