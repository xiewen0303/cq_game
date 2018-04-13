package com.junyou.bus.huajuan2.configure;

import java.util.Map;

public class Huajuan2LieBiaoConfig {
	private int id;
	private int needLevel;
	private Map<String, Long> attrs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNeedLevel() {
		return needLevel;
	}
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	
}
