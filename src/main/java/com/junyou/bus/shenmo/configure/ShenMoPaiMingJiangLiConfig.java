package com.junyou.bus.shenmo.configure;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class ShenMoPaiMingJiangLiConfig extends AbsVersion {
	private Integer id;
	private Integer min;
	private Integer max;
	private String jiangItems;
	private Map<String,Integer> jiangitemMap;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getJiangItems() {
		return jiangItems;
	}

	public void setJiangItems(String jiangItems) {
		this.jiangItems = jiangItems;
	}

	public Map<String, Integer> getJiangitemMap() {
		return jiangitemMap;
	}

	public void setJiangitemMap(Map<String, Integer> jiangitemMap) {
		this.jiangitemMap = jiangitemMap;
	}
	
}
