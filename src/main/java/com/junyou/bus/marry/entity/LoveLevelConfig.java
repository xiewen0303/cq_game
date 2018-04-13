package com.junyou.bus.marry.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-8-10 下午9:56:11
 */
public class LoveLevelConfig {
	private int level;
	private String itemId1;
	private int itemGold;
	private int itemBGold;
	private int count;
	private int needMoney;
	private int addLove;
	private int maxLove;
	private Map<String,Long> atts;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	public int getAddLove() {
		return addLove;
	}
	public void setAddLove(int addLove) {
		this.addLove = addLove;
	}
	public int getMaxLove() {
		return maxLove;
	}
	public void setMaxLove(int maxLove) {
		this.maxLove = maxLove;
	}
	public Map<String, Long> getAtts() {
		return atts;
	}
	public void setAtts(Map<String, Long> atts) {
		this.atts = new ReadOnlyMap<>(atts);
	}
	public int getItemGold() {
		return itemGold;
	}
	public void setItemGold(int itemGold) {
		this.itemGold = itemGold;
	}
	public int getItemBGold() {
		return itemBGold;
	}
	public void setItemBGold(int itemBGold) {
		this.itemBGold = itemBGold;
	}
	
}
