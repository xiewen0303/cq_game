package com.junyou.stage.configure.export.impl;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * PK惩罚配置
 * @author LiuYu
 * 2015-6-12 下午6:51:13
 */
public class PkDebuffConfig {
	private int min;
	private Map<String,Long> attribute;
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	
	public Map<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	
}
