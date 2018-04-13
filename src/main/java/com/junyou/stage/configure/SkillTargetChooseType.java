package com.junyou.stage.configure;


public enum SkillTargetChooseType {
	
	ENEMY,
	NO_ENEMY,
	ENEMY_OR_FABAO,
	ENEMY_AOE,
	SAME_CAMP,
	SELF,
	TAFANG;
	
	public static boolean isSelf(SkillTargetChooseType targetType) {
		return SELF.equals(targetType);
	}

	public static boolean isSingleTarget(SkillTargetChooseType targetType) {
		return ENEMY.equals(targetType);
	}

	public static boolean isAOE(SkillTargetChooseType targetType) {
		return ENEMY_AOE.equals(targetType);
	}
	public static boolean isSameCamp(SkillTargetChooseType targetType) {
		return SAME_CAMP.equals(targetType);
	}
	
	public static boolean isNoEnemy(SkillTargetChooseType targetType){
		return NO_ENEMY.equals(targetType);
	}
	
	public static boolean isTaFang(SkillTargetChooseType targetType){
		return TAFANG.equals(targetType);
	}
}
