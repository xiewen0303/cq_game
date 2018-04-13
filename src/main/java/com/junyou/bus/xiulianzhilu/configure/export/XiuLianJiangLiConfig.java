package com.junyou.bus.xiulianzhilu.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class XiuLianJiangLiConfig extends AbsVersion {

	
	private Integer id;
	private Integer jlType;//奖励类型（积分或每日）
	private Integer day;//第几天
	private Integer jlLevel;//奖励等级
	private Map<String, Integer> spItem;//特别大奖和每日奖励
	private Integer time;//活动持续天数
	private Integer jfJiaZhi;//积分价值
	private Integer chenghaoJifen;//兑换称号所需积分
	private Map<Integer, String[]> ptItem;//普通奖励（map<第几个，object[]{物品ID,数量,所需积分}>）
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getJlType() {
		return jlType;
	}
	public void setJlType(Integer jlType) {
		this.jlType = jlType;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getJlLevel() {
		return jlLevel;
	}
	public void setJlLevel(Integer jlLevel) {
		this.jlLevel = jlLevel;
	}
	public Map<String, Integer> getSpItem() {
		return spItem;
	}
	public void setSpItem(Map<String, Integer> spItem) {
		this.spItem = spItem;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Integer getJfJiaZhi() {
		return jfJiaZhi;
	}
	public void setJfJiaZhi(Integer jfJiaZhi) {
		this.jfJiaZhi = jfJiaZhi;
	}
	public Integer getChenghaoJifen() {
		return chenghaoJifen;
	}
	public void setChenghaoJifen(Integer chenghaoJifen) {
		this.chenghaoJifen = chenghaoJifen;
	}
	public Map<Integer, String[]> getPtItem() {
		return ptItem;
	}
	public void setPtItem(Map<Integer, String[]> ptItem) {
		this.ptItem = ptItem;
	}
	
}
