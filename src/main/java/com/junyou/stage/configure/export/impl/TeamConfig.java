package com.junyou.stage.configure.export.impl;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

public class TeamConfig {
	private int count;
	private ReadOnlyMap<String, Long> attribute;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ReadOnlyMap<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	
}
