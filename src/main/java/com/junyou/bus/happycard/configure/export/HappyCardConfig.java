package com.junyou.bus.happycard.configure.export;

import java.util.HashMap;
import java.util.Map;

public class HappyCardConfig {
	private int id;
	private int totalWeight;
	private Map<Integer, HappyCardItem> items = new HashMap<Integer, HappyCardItem>();
	private Map<Integer, Integer> weightMap = new HashMap<Integer, Integer>();

	private int money;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Integer, HappyCardItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, HappyCardItem> items) {
		this.items = items;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Map<Integer, Integer> getWeightMap() {
		return weightMap;
	}

	public void setWeightMap(Map<Integer, Integer> weightMap) {
		this.weightMap = weightMap;
	}

	public HappyCardItem getItem(Integer index) {
		return items.get(index);
	}

}
