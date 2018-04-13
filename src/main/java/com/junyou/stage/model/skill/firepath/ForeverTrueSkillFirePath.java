package com.junyou.stage.model.skill.firepath;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;


/**
 * 永远选中
 * @author DaoZheng Yuan
 * 2013-10-31 下午8:14:03
 */
public class ForeverTrueSkillFirePath implements ISkillFirePath {

	@Override
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,
			Object[] helpParam, IStage stage, ConfirmDraft draft) {
		return true;
	}
	
	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,
			IFighter attacker, IFighter target) {
		// TODO Auto-generated method stub
		return new Object[]{target.getPosition().getX(), target.getPosition().getY()};
	}
}
