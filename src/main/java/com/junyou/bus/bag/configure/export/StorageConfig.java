package com.junyou.bus.bag.configure.export;

import java.util.Map;

/**
 * 仓库格位扩容表
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:27:16
  *@Description:
 */
public class StorageConfig  {

	private Integer id;
	private int time;
	private int needMoney;//元宝
	private Map<String,Long> attrs;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getNeedMoney() {
		return needMoney;
	}
	public void setNeedMoney(int needMoney) {
		this.needMoney = needMoney;
	}
	public Map<String, Long> getAttrs() {
		return attrs;
	}
	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}
	
}
