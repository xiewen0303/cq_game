package com.junyou.bus.shizhuang.configure.export;

import java.util.Map;


/**
 * 
 * @description 时装进阶阶段
 *
 * @author ChenXiaobing
 * @date 2016-11-21 14:55:39
 */
public class ShiZhuangJieDuanConfig{

	private int id;
	private int level;
	private int shizhuangshuxing;
	private Map<String,Long> attrs;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getShizhuangshuxing() {
		return shizhuangshuxing;
	}
	public void setShizhuangshuxing(int shizhuangshuxing) {
		this.shizhuangshuxing = shizhuangshuxing;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	
}
