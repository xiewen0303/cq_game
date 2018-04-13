package com.junyou.bus.caidan.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaidanCategoryConfig {

	private Integer restetGold;//重置所需元宝数
	private Object[] showItems;
	private String version;
	private String bg;
	private String info;
	private List<CaidanConfig> list = new ArrayList<CaidanConfig>();
	private CaidanConfig lastConfig;
	private Map<String,Integer> duihuanMap;
	private Object[] getDuihuanInfo;
	private Integer maxCount = 9999;
	
	public void setShowItems(Object[] showItems) {
		this.showItems = showItems;
	}
	public CaidanConfig getCaidanConfig(int count) {
		for (CaidanConfig config : list) {
			if(count <= config.getId()){
				return config;
			}
		}
		return lastConfig;
	}
	public void addCaidan(CaidanConfig caidan){
		list.add(caidan);
		lastConfig = caidan;
	}
	public Integer getRestetGold() {
		return restetGold;
	}
	public void setRestetGold(Integer restetGold) {
		this.restetGold = restetGold;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Map<String, Integer> getDuihuanMap() {
		return duihuanMap;
	}
	public void setDuihuanMap(Map<String, Integer> duihuanMap) {
		this.duihuanMap = duihuanMap;
	}
	public Object[] getGetDuihuanInfo() {
		return getDuihuanInfo;
	}
	public void setGetDuihuanInfo(Object[] getDuihuanInfo) {
		this.getDuihuanInfo = getDuihuanInfo;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Object[] getShowItems() {
		return showItems;
	}
	public String getBg() {
		return bg;
	}
	public String getInfo() {
		return info;
	}
	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	
	
}
