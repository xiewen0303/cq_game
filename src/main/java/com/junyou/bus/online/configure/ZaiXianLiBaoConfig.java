package com.junyou.bus.online.configure;

import java.util.HashMap;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 在线奖励基础配置
 */
public class ZaiXianLiBaoConfig extends AbsVersion {

	private Integer day;
	private Map<Integer,Map<String,Integer>> awards = new HashMap<Integer, Map<String,Integer>>();
	private int[] keys;
	
	public int[] getKeys() {
		return keys;
	}
	public void setKeys(int[] keys) {
		this.keys = keys;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Map<Integer, Map<String, Integer>> getAwards() {
		return awards;
	}
	public void setAwards(Map<Integer, Map<String, Integer>> awards) {
		this.awards = awards;
	}
}
