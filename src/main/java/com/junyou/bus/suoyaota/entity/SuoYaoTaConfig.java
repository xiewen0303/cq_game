package com.junyou.bus.suoyaota.entity;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;

/**
 * @author LiuYu
 * 2015-7-28 上午10:44:17
 */
public class SuoYaoTaConfig {
	private int id;
	private String version;
	private Map<Integer,SuoYaoTaCengConfig> cengInfo;
	private int cost;
	private String bg;
	private String info;
	private Integer maxCount = 9999;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Map<Integer, SuoYaoTaCengConfig> getCengInfo() {
		return cengInfo;
	}
	public void setCengInfo(Map<Integer, SuoYaoTaCengConfig> cengInfo) {
		this.cengInfo = new ReadOnlyMap<>(cengInfo);
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	
}
