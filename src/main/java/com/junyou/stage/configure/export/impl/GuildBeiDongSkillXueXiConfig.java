package com.junyou.stage.configure.export.impl;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 门派被动技能学习表
 * @author LiuYu
 * @date 2015-7-20 下午10:16:39
 */
public class GuildBeiDongSkillXueXiConfig {
	
	private int level;
	private ReadOnlyMap<String, Long> attributes;
	private int needLevel;
	private int minLevel;
	private int zhenqi;
	private int gold;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public ReadOnlyMap<String, Long> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Long> attributes) {
		this.attributes = new ReadOnlyMap<>(attributes);
	}
	public int getMinLevel() {
		return minLevel;
	}
	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
	public int getZhenqi() {
		return zhenqi;
	}
	public void setZhenqi(int zhenqi) {
		this.zhenqi = zhenqi;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	
	
}
