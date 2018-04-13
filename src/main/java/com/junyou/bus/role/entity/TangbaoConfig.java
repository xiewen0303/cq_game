package com.junyou.bus.role.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiuYu
 * 2015-5-1 下午3:16:23
 */
public class TangbaoConfig {
	private int level;
	private Map<String, Long> attribute;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String, Long> getAttribute() {
		return new HashMap<>(attribute);
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = attribute;
	}
	
}
