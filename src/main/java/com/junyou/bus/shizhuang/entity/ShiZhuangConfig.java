package com.junyou.bus.shizhuang.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-8-7 下午2:26:37
 */
public class ShiZhuangConfig implements IGoodsCheckConfig{
	private int id;
	private Map<String,Integer> costItem;
	private int gold;
	private int sex;
	private Map<String,Long> attribute;
	private int xftime;
	private int xfgold;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<String, Integer> getCostItem() {
		return costItem;
	}
	public void setCostItem(Map<String, Integer> costItem) {
		this.costItem = new ReadOnlyMap<>(costItem);
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public Map<String, Long> getAttribute() {
		return attribute;
	}
	public void setAttribute(Map<String, Long> attribute) {
		this.attribute = new ReadOnlyMap<>(attribute);
	}
	@Override
	public String getConfigName() {
		return "ShiZhuangConfig--"+id;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = new ArrayList<>();
		list.add(costItem);
		return list;
	}
	public int getXftime() {
		return xftime;
	}
	public void setXftime(int xftime) {
		this.xftime = xftime;
	}
	public int getXfgold() {
		return xfgold;
	}
	public void setXfgold(int xfgold) {
		this.xfgold = xfgold;
	}
	
}
