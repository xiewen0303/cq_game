package com.junyou.bus.huajuan2.configure;

import java.util.Map;

public class Huajuan2XinXiBiaoConfig {
	private int id;
	private int liebiaoId;
	private int star;
	private Map<String,Integer> needitem;
	private Map<String,Long> attrs;
	private Huajuan2XinXiBiaoConfig nextConfig;
	private String monsterId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLiebiaoId() {
		return liebiaoId;
	}
	public void setLiebiaoId(int liebiaoId) {
		this.liebiaoId = liebiaoId;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public Map<String, Integer> getNeeditem() {
		return needitem;
	}
	public void setNeeditem(Map<String, Integer> needitem) {
		this.needitem = needitem;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	public Huajuan2XinXiBiaoConfig getNextConfig() {
		return nextConfig;
	}
	public void setNextConfig(Huajuan2XinXiBiaoConfig nextConfig) {
		this.nextConfig = nextConfig;
	}
	public String getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}
}
