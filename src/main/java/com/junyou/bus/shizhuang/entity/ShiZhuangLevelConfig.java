package com.junyou.bus.shizhuang.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-8-7 下午2:38:19
 */
public class ShiZhuangLevelConfig implements IGoodsCheckConfig{
	private int level;
	private Map<String,Integer> items;
	private Map<String,Long> atts;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String, Integer> getItems() {
		return items;
	}
	public void setItems(Map<String, Integer> items) {
		this.items = new ReadOnlyMap<>(items);
	}
	public Map<String, Long> getAtts() {
		return atts;
	}
	public void setAtts(Map<String, Long> atts) {
		this.atts = new ReadOnlyMap<>(atts);
	}
	@Override
	public String getConfigName() {
		return "ShiZhuangLevelConfig--"+level;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = new ArrayList<>();
		list.add(items);
		return list;
	}
	
}
