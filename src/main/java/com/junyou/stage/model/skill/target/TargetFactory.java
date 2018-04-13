package com.junyou.stage.model.skill.target;

import java.util.HashMap;
import java.util.Map;

import com.junyou.stage.configure.SkillTargetChooseType;
import com.junyou.stage.model.core.skill.ISkillTarget;

/**
 * 
 * @description 目标计算规则工厂
 *
 * @author LiuJuan
 * @created 2011-12-13 下午04:40:29
 */
public class TargetFactory {
	
	private static Map<SkillTargetChooseType, ISkillTarget> targets = new HashMap<SkillTargetChooseType, ISkillTarget>();
	
	static {
		targets.put(SkillTargetChooseType.ENEMY, new EnemyTarget());
		targets.put(SkillTargetChooseType.SELF, new SelfTarget());
		targets.put(SkillTargetChooseType.ENEMY_AOE, new EnemyAoeTarget());
		targets.put(SkillTargetChooseType.NO_ENEMY, new NoEnemyTarget());
		targets.put(SkillTargetChooseType.SAME_CAMP, new SameCampTarget());
	}
	
	/**
	 * 获以目标计算规则
	 * @param targetType
	 * @return ISkillTarget
	 */
	public static ISkillTarget getSkillTarget(SkillTargetChooseType targetType) {
		
		ISkillTarget skillTarget = targets.get(targetType);
		if(null == skillTarget){
			skillTarget = targets.get(SkillTargetChooseType.ENEMY_OR_FABAO);
		}
		
		return skillTarget;
	}

}
