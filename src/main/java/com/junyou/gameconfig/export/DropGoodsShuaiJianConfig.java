package com.junyou.gameconfig.export;


/**
 * 
 * @description 掉落物品等级衰减 
 *
 * @author LiuJuan
 * @date 2012-3-30 下午1:26:54
 */
public class DropGoodsShuaiJianConfig{
	
	private int monsterType;
	private int levelDifference;
	
	public int getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(int monsterType) {
		this.monsterType = monsterType;
	}
	public int getLevelDifference() {
		return levelDifference;
	}
	public void setLevelDifference(int levelDifference) {
		this.levelDifference = levelDifference;
	}
}
