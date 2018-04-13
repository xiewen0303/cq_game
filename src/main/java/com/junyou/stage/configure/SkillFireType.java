package com.junyou.stage.configure;

public enum SkillFireType {
	/**
	 *  0:普通技能
		1:怒气技能
		2:仙宫探宝技能
		3:神器技能
		4:BUFF触发效果技能
		6:妖神技能
		9:特殊技能，可以产生位移
	 */
	
	NORMAL(0),NUQI(1),XIANGONG(2),SHENQI(3),BUFF(4),YAOSHEN(6),TESHU(9);
	
	
	private Integer val;
	
	private SkillFireType(Integer val){
		this.val = val;
	}
	
	public Integer getVal() {
		return val;
	}
	
	
	public static SkillFireType getSkillFireTypeByVal(int val){
		switch (val) {
		
		case 1:
			
			return NUQI;
		case 2:
			
			return XIANGONG;
		case 3:
			
			return SHENQI;
		case 4:
			
			return BUFF;
		case 6:
			
			return YAOSHEN;
		default:
			
			return NORMAL;
		}
	}
	
	public static boolean isNoChangeClearSkill(SkillFireType type){
		return NUQI.equals(type) || SHENQI.equals(type);
	}
}
