package com.junyou.bus.cuxiao.configure.export;

import java.util.Map;

public class CuxiaoConfig {

	private int id;
	private int taskId;
	private int type;
	private int id1;
	private Map<String, Integer> rewards;
	private String data1;
	private int consumeGold;
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getData1() {
		return data1;
	}
	public void setData1(String data1) {
		this.data1 = data1;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public Map<String, Integer> getRewards() {
		return rewards;
	}
	public void setRewards(Map<String, Integer> rewards) {
		this.rewards = rewards;
	}
	public int getConsumeGold() {
		return consumeGold;
	}
	public void setConsumeGold(int consumeGold) {
		this.consumeGold = consumeGold;
	}
	
}
