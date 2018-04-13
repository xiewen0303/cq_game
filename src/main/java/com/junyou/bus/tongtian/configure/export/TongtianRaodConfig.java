package com.junyou.bus.tongtian.configure.export;

import java.util.Map;

public class TongtianRaodConfig {

	private int id;
	private int cuilianValue;// 淬炼上限值
	private long maxAttr; // 属性上限值
	private long maxZplus; // 战力上限值
	private String attrName;//属性名称
	private Map<String, Long> attrMap;
	 
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	 public long getMaxAttr() {
		return maxAttr;
	}
	 public void setMaxAttr(long maxAttr) {
		this.maxAttr = maxAttr;
	}
	 public long getMaxZplus() {
		return maxZplus;
	}
	 public void setMaxZplus(long maxZplus) {
		this.maxZplus = maxZplus;
	}

	public Map<String, Long> getAttrMap() {
		return attrMap;
	}

	public void setAttrMap(Map<String, Long> attrMap) {
		this.attrMap = attrMap;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCuilianValue() {
		return cuilianValue;
	}

	public void setCuilianValue(int cuilianValue) {
		this.cuilianValue = cuilianValue;
	}
}
