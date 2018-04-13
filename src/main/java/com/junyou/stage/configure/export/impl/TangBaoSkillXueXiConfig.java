package com.junyou.stage.configure.export.impl;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 被动技能学习表
 * @author LiuYu
 * @date 2015-3-11 上午10:24:13
 */
public class TangBaoSkillXueXiConfig {
	
	private int level;
	private ReadOnlyMap<String, Long> attributes;
	private String needItem;
	private int minLevel;
	private int zhenqi;
	private int gold;
	private Integer count;
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
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
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
	public String getNeedItem() {
		return needItem;
	}
	public void setNeedItem(String needItem) {
		this.needItem = needItem;
	}
	
	
}
