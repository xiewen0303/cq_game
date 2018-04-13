package com.junyou.bus.chengshen.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 成神--称号
 */
public class ChengShenTitleConfig extends AbsVersion {

	private int level;
	private int needValue;
	private int chengHaoId;
	private Map<String, Long> attrMap;

	public int getChengHaoId() {
		return chengHaoId;
	}

	public void setChengHaoId(int chengHaoId) {
		this.chengHaoId = chengHaoId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNeedValue() {
		return needValue;
	}

	public void setNeedValue(int needValue) {
		this.needValue = needValue;
	}

	public Map<String, Long> getAttrMap() {
		return attrMap;
	}

	public void setAttrMap(Map<String, Long> attrMap) {
		this.attrMap = attrMap;
	}
}
