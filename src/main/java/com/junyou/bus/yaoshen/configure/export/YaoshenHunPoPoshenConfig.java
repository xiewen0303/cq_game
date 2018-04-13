package com.junyou.bus.yaoshen.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class YaoshenHunPoPoshenConfig extends AbsVersion {
	private String id;
	private String goodId;
	private int  id1;
	private Map<String, Long> attMap;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public Map<String, Long> getAttMap() {
		return attMap;
	}
	public void setAttMap(Map<String, Long> attMap) {
		this.attMap = attMap;
	}
}
