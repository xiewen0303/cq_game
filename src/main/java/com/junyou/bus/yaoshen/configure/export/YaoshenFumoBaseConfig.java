package com.junyou.bus.yaoshen.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class YaoshenFumoBaseConfig extends AbsVersion { 

	private int configId;
	private int id1;
	private int id2;
	private int id;
	private int level;
	private String consumeitemId;
	private int consumeCount;
	private Map<String, Integer> consumeMap;
	private int needMoney;
	private Map<String, Long> attrMap;
	
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	public int getConfigId() {
		return configId;
	}
	public int getConsumeCount() {
		return consumeCount;
	}
	public void setConsumeCount(int consumeCount) {
		this.consumeCount = consumeCount;
	}
	public String getConsumeitemId() {
		return consumeitemId;
	}

	public void setConsumeitemId(String consumeitemId) {
		this.consumeitemId = consumeitemId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String, Integer> getConsumeMap() {
		return consumeMap;
	}
	public void setConsumeMap(Map<String, Integer> consumeMap) {
		this.consumeMap = consumeMap;
	}
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	public Map<String, Long> getAttrMap() {
		return attrMap;
	}
	public void setAttrMap(Map<String, Long> attrMap) {
		this.attrMap = attrMap;
	}
	

	
}
