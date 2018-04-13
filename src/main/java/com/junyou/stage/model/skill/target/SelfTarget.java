package com.junyou.stage.model.skill.target;

import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.skill.ISkillTarget;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.fight.BattleModeRelation;

public class SelfTarget implements ISkillTarget {

	@Override
	public List<IFighter> chooseTargets(ISkill skill, IFighter fighter,
			IFighter target) {
		
		List<IFighter> result = new ArrayList<IFighter>();
		result.add(fighter);
		
		return result;
	}

	@Override
	public boolean inRange(ISkill skill, IFighter attacker, IFighter target) {
		
		BattleMode battleMode = attacker.getBattleMode();
		if(battleMode==null){
			battleMode = BattleMode.PEACE;
			attacker.setBattleMode(BattleMode.PEACE);
		}
		
		return !BattleModeRelation.isEnemy(battleMode, attacker, target);
	}

}
