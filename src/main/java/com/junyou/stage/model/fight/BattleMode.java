package com.junyou.stage.model.fight;

/**
 * @description 战斗模式
 * @author ShiJie Chi
 * @date 2012-3-28 上午10:29:24 
 */
public enum BattleMode {

	PEACE(0),TEAM(1),GUILD(2),ALL(3), SHAN_E(4),SAME_SERVER(5),MONSTER_NORMAL(9);

	private final Integer val;
	
	private BattleMode(Integer val) {
		this.val = val;
	}
	
	public Integer getVal(){
		return val;
	}
	
	
	/**
	 * 类型转换
	 */
	public static BattleMode convert(int mode) {
		
		switch (mode) {
		case 0:
			return PEACE;
		case 1:
			return TEAM;
		case 2:
			return GUILD;
		case 3:
			return ALL;
		case 4:
			return SHAN_E;
		default:
			return PEACE;
		}
	}
	
}
