package com.junyou.bus.marry.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-8-10 下午9:54:20
 */
public class JieHunConfig {
	private int id;
	private int level;
	private String itemId1;
	private int count;
	private int gold;
	private Map<String,Long> atts;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItemId1() {
		return itemId1;
	}
	public void setItemId1(String itemId1) {
		this.itemId1 = itemId1;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public Map<String, Long> getAtts() {
		return atts;
	}
	public void setAtts(Map<String, Long> atts) {
		this.atts = new ReadOnlyMap<>(atts);
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
