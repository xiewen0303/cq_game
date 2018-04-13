package com.junyou.bus.hefuSevenlogin.configure;

import java.util.Map;

public class HefuSevenLoginConfig  {

	private  Integer id;
	private  long exp;
	private  long money;
	private  Map<String, Integer>  items;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public long getExp() {
		return exp;
	}
	public long getMoney() {
		return money;
	}
	public Map<String, Integer> getItems() {
		return items;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}
}
