package com.junyou.bus.cuxiao.configure.export;

import java.util.Map;

public class JingJieFanLiConfig {

	private int id;
	private int type;
	private int level;
	private int xprice;
	private Map<String, Integer> rewards;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getXprice() {
		return xprice;
	}

	public void setXprice(int xprice) {
		this.xprice = xprice;
	}

	public Map<String, Integer> getRewards() {
		return rewards;
	}

	public void setRewards(Map<String, Integer> rewards) {
		this.rewards = rewards;
	}
}
