package com.junyou.gameconfig.constants;

/**
 * 技能表 ishostile 字段
 * @author DaoZheng Yuan
 * 2013-10-31 下午5:49:04
 */
public enum EnemyHostileType {

	NO_ENEMY,ENEMY,SAME_CAMP;
	
	
	public static EnemyHostileType getEnemyHostileType(int type){
		switch (type) {
		case 1:
			
			return ENEMY;
			
		case 2:
			
			return SAME_CAMP;
		default:
			
			return NO_ENEMY;
		}
		
	}
}
