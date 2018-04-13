package com.junyou.bus.lj.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class LianJinRankConfig extends AbsVersion {
	private int id;
	private int lvl;
	private int maxexp;
	private Map<String,Integer> awards;
	private int basemoney;
	private int extramoney;
	private int basepercent;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLvl() {
		return lvl;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	public int getMaxexp() {
		return maxexp;
	}
	public void setMaxexp(int maxexp) {
		this.maxexp = maxexp;
	}
	public Map<String, Integer> getAwards() {
		return awards;
	}
	public void setAwards(Map<String, Integer> awards) {
		this.awards = awards;
	}
	public int getBasemoney() {
		return basemoney;
	}
	public void setBasemoney(int basemoney) {
		this.basemoney = basemoney;
	}
	public int getExtramoney() {
		return extramoney;
	}
	public void setExtramoney(int extramoney) {
		this.extramoney = extramoney;
	}
	public int getBasepercent() {
		return basepercent;
	}
	public void setBasepercent(int basepercent) {
		this.basepercent = basepercent;
	}
}
