package com.junyou.bus.equip.configure.export;

import java.util.Map;

/**
 * 装备强化加成表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:27:16
  *@Description:
 */
public class ZhuangBeiQiangHuaJiaChengConfig  {
	
	private int id;
	private int type;
	private int lv; 
	private Map<String,Long> attrs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
}
