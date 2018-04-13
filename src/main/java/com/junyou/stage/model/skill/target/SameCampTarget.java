package com.junyou.stage.model.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillTarget;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.BattleModeRelation;

public class SameCampTarget implements ISkillTarget {

	@Override
	public List<IFighter> chooseTargets(ISkill skill, IFighter fighter,IFighter target) {
		
		List<IFighter> result = new ArrayList<IFighter>();
		result.add(target);	
		
		return result;
	}

	@Override
	public boolean inRange(ISkill skill, IFighter attacker, IFighter target) {
		BattleMode battleMode = attacker.getBattleMode();
		
		return BattleModeRelation.isSameCamp(battleMode, attacker, target);
	}

}
