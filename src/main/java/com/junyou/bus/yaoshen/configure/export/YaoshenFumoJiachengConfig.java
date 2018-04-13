package com.junyou.bus.yaoshen.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class YaoshenFumoJiachengConfig extends AbsVersion { 

	private int id;
	private int fumolevel;
	private Map<String, Long> attrMap;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFumolevel() {
		return fumolevel;
	}
	public void setFumolevel(int fumolevel) {
		this.fumolevel = fumolevel;
	}
	public Map<String, Long> getAttrMap() {
		return attrMap;
	}
	public void setAttrMap(Map<String, Long> attrMap) {
		this.attrMap = attrMap;
	}
	
	
	
}
