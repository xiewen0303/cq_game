package com.junyou.bus.suoyaota.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-7-28 上午10:44:17
 */
public class SuoYaoTaCengConfig {
	private Map<Integer,Integer> slotOdds;//格位权重
	private int slotAllOdd;//格位权重总值
	private Map<Integer,SuoYaoTaSlotConfig> slotInfo;
	private int maxLucky;
	public Map<Integer, Integer> getSlotOdds() {
		return slotOdds;
	}
	public void setSlotOdds(Map<Integer, Integer> slotOdds) {
		this.slotOdds = new ReadOnlyMap<>(slotOdds);
	}
	public int getSlotAllOdd() {
		return slotAllOdd;
	}
	public void setSlotAllOdd(int slotAllOdd) {
		this.slotAllOdd = slotAllOdd;
	}
	public Map<Integer, SuoYaoTaSlotConfig> getSlotInfo() {
		return slotInfo;
	}
	public void setSlotInfo(Map<Integer, SuoYaoTaSlotConfig> slotInfo) {
		this.slotInfo = new ReadOnlyMap<>(slotInfo);
	}
	public int getMaxLucky() {
		return maxLucky;
	}
	public void setMaxLucky(int maxLucky) {
		this.maxLucky = maxLucky;
	}
	
}
