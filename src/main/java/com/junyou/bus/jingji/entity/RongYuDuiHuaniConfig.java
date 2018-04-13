package com.junyou.bus.jingji.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.checker.IGoodsCheckConfig;

public class RongYuDuiHuaniConfig implements IGoodsCheckConfig{
	private String itemId;
	private int needRongyu;
	private int needLevel;
	private int maxCount;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public int getNeedRongyu() {
		return needRongyu;
	}
	public void setNeedRongyu(int needRongyu) {
		this.needRongyu = needRongyu;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	@Override
	public String getConfigName() {
		return "RongYuDuiHuaniConfig--" + itemId;
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = new ArrayList<>();
		Map<String, Integer> map = new HashMap<>();
		map.put(itemId, 1);
		list.add(map);
		return list;
	}
	
}
