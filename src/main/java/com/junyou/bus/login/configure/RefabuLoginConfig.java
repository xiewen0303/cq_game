package com.junyou.bus.login.configure;

import java.util.Map;

/**
 * 
 * @description 七日开服活动配置表 (全民修仙)
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class RefabuLoginConfig {
	
	private Integer id;
	
	private Map<String, Integer> item;
	
	private String clientItem;
	
	public String getClientItem() {
		return clientItem;
	}
	public void setClientItem(String clientItem) {
		this.clientItem = clientItem;
	}
	public Map<String, Integer> getItem() {
		return item;
	}
	public void setItem(Map<String, Integer> item) {
		this.item = item;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
