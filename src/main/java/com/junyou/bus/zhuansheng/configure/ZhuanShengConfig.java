package com.junyou.bus.zhuansheng.configure;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-11-2 下午2:21:05
 */
public class ZhuanShengConfig {
	private int level;
	private int needLv;
	private int xiuwei;
	private String id1;
	private int count;
	private Map<String,Long> attribute;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNeedLv() {
		return needLv;
	}
	public void setNeedLv(int needLv) {
		this.needLv = needLv;
	}
	public int getXiuwei() {
		return xiuwei;
	}
	public void setXiuwei(int xiuwei) {
		this.xiuwei = xiuwei;
	}
	public Map<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	public String getId1() {
		return id1;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
