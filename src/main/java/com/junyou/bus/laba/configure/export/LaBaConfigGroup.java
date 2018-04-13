package com.junyou.bus.laba.configure.export;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 拉霸活动配置表 
 */
public class LaBaConfigGroup {
	private	int    id; 
	private	String desc;	//描述
	private String bg;		//背景
	private String md5Version;
	private Map<Integer, LaBaConfig>  labaConfigs  = new LinkedHashMap<>();
	private int recordCount;
	private int maxId;
	
	public int getMaxId() {
		return maxId;
	}
	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}
	public String getMd5Version() {
		return md5Version;
	}
	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, LaBaConfig> getLabaConfigs() {
		return labaConfigs;
	}
	public void putIntoMap(int id,LaBaConfig laBaConfig) {
		this.labaConfigs.put(id, laBaConfig);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getBg() {
		return bg;
	}
	public void setBg(String bg) {
		this.bg = bg;
	}
	public void setLabaConfigs(Map<Integer, LaBaConfig> labaConfigs) {
		this.labaConfigs = labaConfigs;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}  
}
