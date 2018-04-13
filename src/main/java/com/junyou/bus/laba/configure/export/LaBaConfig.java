package com.junyou.bus.laba.configure.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaBaConfig {
	private int id;
	private int count;					//次数
	private int beginIndex;
	private int endIndex;
	private int chargenum;
	private int vipLevel;
	private int needGold;
	private int mingain;
	private List<LaBaInfo> laBaInfo;
	
	public Map<int[],Integer> getWeightVals(){
		Map<int[],Integer> result = new HashMap<>();
		for (LaBaInfo element : laBaInfo) {
			result.put(new int[]{element.getRangemin(),element.getRangemax()}, element.getWeight());
		}
		return result;
	}
	
	public int getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public int getMingain() {
		return mingain;
	}
	public void setMingain(int mingain) {
		this.mingain = mingain;
	}
	public int getId() {
		return id;
	} 
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getChargenum() {
		return chargenum;
	}
	public void setChargenum(int chargenum) {
		this.chargenum = chargenum;
	}
	public int getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}
	public int getNeedGold() {
		return needGold;
	}
	public void setNeedGold(int needGold) {
		this.needGold = needGold;
	}
	public List<LaBaInfo> getLaBaInfo() {
		return laBaInfo;
	}
	public void setLaBaInfo(List<LaBaInfo> laBaInfo) {
		this.laBaInfo = laBaInfo;
	}
}

class LaBaInfo{
	 
	private int rangemin;
	private int rangemax;
	private int weight;
	
	public int getRangemin() {
		return rangemin;
	}
	public void setRangemin(int rangemin) {
		this.rangemin = rangemin;
	}
	public int getRangemax() {
		return rangemax;
	}
	public void setRangemax(int rangemax) {
		this.rangemax = rangemax;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}