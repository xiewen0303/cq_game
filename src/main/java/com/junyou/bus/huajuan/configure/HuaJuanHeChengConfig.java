package com.junyou.bus.huajuan.configure;

import java.util.Map;

public class HuaJuanHeChengConfig {
	private int id;
	private String item;
	private Map<String,Integer> needitem;
	private int needmoney;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Map<String, Integer> getNeeditem() {
		return needitem;
	}
	public void setNeeditem(Map<String, Integer> needitem) {
		this.needitem = needitem;
	}
	public int getNeedmoney() {
		return needmoney;
	}
	public void setNeedmoney(int needmoney) {
		this.needmoney = needmoney;
	}
	
}
