package com.junyou.bus.tafang.configure;

import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-10-12 下午4:45:56
 */
public class TaFangPublicConfig extends AdapterPublicConfig{
	private int map;
	private int count;
	/**使用银两升级所需等级*/
	private int moneyLevel;
	/**使用银两升级所需VIP等级*/
	private int moneyVip;
	/**每只怪物刷新间隔（毫秒）*/
	private int perMonsterTime;
	/**每波怪物刷新间隔（毫秒）*/
	private int monstersTime;
	private Map<String,Integer> sendGift;
	public int getMap() {
		return map;
	}
	public void setMap(int map) {
		this.map = map;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMoneyLevel() {
		return moneyLevel;
	}
	public void setMoneyLevel(int moneyLevel) {
		this.moneyLevel = moneyLevel;
	}
	public int getMoneyVip() {
		return moneyVip;
	}
	public void setMoneyVip(int moneyVip) {
		this.moneyVip = moneyVip;
	}
	public int getPerMonsterTime() {
		return perMonsterTime;
	}
	public void setPerMonsterTime(int perMonsterTime) {
		this.perMonsterTime = perMonsterTime;
	}
	public int getMonstersTime() {
		return monstersTime;
	}
	public void setMonstersTime(int monstersTime) {
		this.monstersTime = monstersTime;
	}
	public Map<String, Integer> getSendGift() {
		return sendGift;
	}
	public void setSendGift(Map<String, Integer> sendGift) {
		this.sendGift = new ReadOnlyMap<>(sendGift);
	}
	
}
