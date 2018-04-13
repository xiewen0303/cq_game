package com.junyou.bus.hongbao.configure;

import java.util.Map;

public class HongbaoConfig {
	private int id;
	private int gold; // 再次抽取花费id=-1;
	private int gailv;// 红包掉率概率id =-1;
	private int type;
	private int count; // 总权重
	private Map<String, Integer> dataMap; // {权重值：10001:2,...}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getGailv() {
		return gailv;
	}

	public void setGailv(int gailv) {
		this.gailv = gailv;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

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

	public Map<String, Integer> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Integer> dataMap) {
		this.dataMap = dataMap;
	}

}
