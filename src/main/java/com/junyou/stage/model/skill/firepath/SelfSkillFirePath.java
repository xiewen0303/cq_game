package com.junyou.stage.model.skill.firepath;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;

/**
 * @description 自己
 * @author ShiJie Chi
 * @date 2012-6-12 下午4:19:24 
 */
public class SelfSkillFirePath implements ISkillFirePath {

	@Override
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParamsm, IStage stage,ConfirmDraft draft) {
		
		return attacker.getId().equals(target.getId());
		
	}

	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		// TODO Auto-generated method stub
		
		return null;
		
	}

}
