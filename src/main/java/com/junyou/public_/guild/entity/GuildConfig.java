package com.junyou.public_.guild.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

public class GuildConfig {
	private int level;
	/**升级所需银两*/
	private int needMoney;
	/**升级令牌1*/
	private int needItem1;
	/**升级令牌2*/
	private int needItem2;
	/**升级令牌3*/
	private int needItem3;
	/**升级令牌4*/
	private int needItem4;
	
	private int maxCount;
	private ReadOnlyMap<String, Long> attribute;
	private int addExp;
	private int addZhenqi;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public ReadOnlyMap<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	public int getNeedItem1() {
		return needItem1;
	}
	public void setNeedItem1(int needItem1) {
		this.needItem1 = needItem1;
	}
	public int getNeedItem2() {
		return needItem2;
	}
	public void setNeedItem2(int needItem2) {
		this.needItem2 = needItem2;
	}
	public int getNeedItem3() {
		return needItem3;
	}
	public void setNeedItem3(int needItem3) {
		this.needItem3 = needItem3;
	}
	public int getNeedItem4() {
		return needItem4;
	}
	public void setNeedItem4(int needItem4) {
		this.needItem4 = needItem4;
	}
	public int getAddExp() {
		return addExp;
	}
	public void setAddExp(int addExp) {
		this.addExp = addExp;
	}
	public int getAddZhenqi() {
		return addZhenqi;
	}
	public void setAddZhenqi(int addZhenqi) {
		this.addZhenqi = addZhenqi;
	}
	
}
