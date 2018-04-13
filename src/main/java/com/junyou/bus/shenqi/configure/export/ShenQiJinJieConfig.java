package com.junyou.bus.shenqi.configure.export;

import java.util.Map;

public class ShenQiJinJieConfig {
	// 神器id
	private Integer id;
	// 神器名
	private String name;
	private Integer level;
	private Integer count;
	private String consumeId;
	private String mallId;
	private Integer gold;
	private Integer bgold;
	private Integer zfzMin;
	private Integer zfzMax;
	private Integer successRate;
	private Integer zfzMinAdd;
	private Integer zfzMaxAdd;
	private Map<String, Long> attrs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getConsumeId() {
		return consumeId;
	}

	public void setConsumeId(String consumeId) {
		this.consumeId = consumeId;
	}

	public String getMallId() {
		return mallId;
	}

	public void setMallId(String mallId) {
		this.mallId = mallId;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getBgold() {
		return bgold;
	}

	public void setBgold(Integer bgold) {
		this.bgold = bgold;
	}

	public Integer getZfzMin() {
		return zfzMin;
	}

	public void setZfzMin(Integer zfzMin) {
		this.zfzMin = zfzMin;
	}

	public Integer getZfzMax() {
		return zfzMax;
	}

	public void setZfzMax(Integer zfzMax) {
		this.zfzMax = zfzMax;
	}

	public Integer getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(Integer successRate) {
		this.successRate = successRate;
	}

	public Integer getZfzMinAdd() {
		return zfzMinAdd;
	}

	public void setZfzMinAdd(Integer zfzMinAdd) {
		this.zfzMinAdd = zfzMinAdd;
	}

	public Integer getZfzMaxAdd() {
		return zfzMaxAdd;
	}

	public void setZfzMaxAdd(Integer zfzMaxAdd) {
		this.zfzMaxAdd = zfzMaxAdd;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
}
