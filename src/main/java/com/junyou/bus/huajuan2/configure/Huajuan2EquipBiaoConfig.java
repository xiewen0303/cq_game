package com.junyou.bus.huajuan2.configure;

import java.util.Map;

public class Huajuan2EquipBiaoConfig {
	private int id;
	private int star;
	private Map<String,Long> attrs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	
}
