package com.junyou.stage.model.skill.firepath;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillFirePath;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.fight.SkillProcessHelper.ConfirmDraft;


/**
 * 塔防攻击
 * @author LiuYu
 * @date 2015-10-9 下午5:17:29
 */
public class TaFangSkillFirePath implements ISkillFirePath {

	@Override
	public boolean inRange(ISkill skill, IStageElement attacker, IStageElement target,Object[] helpParam, IStage stage, ConfirmDraft draft) {
		return true;
	}
	
	@Override
	public Object[] createHelpParams(IStage stage, ISkill skill,IFighter attacker, IFighter target) {
		if(!StageType.TAFANG_FUBEN.equals(stage.getStageType())){
			return null;
		}
		if(!ElementType.isMonster(attacker.getElementType())){
			return null;
		}
		return new Object[]{attacker.getPosition().getX(), attacker.getPosition().getY()};
	}
}
