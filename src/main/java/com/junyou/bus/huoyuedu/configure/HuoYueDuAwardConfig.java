package com.junyou.bus.huoyuedu.configure;

import java.util.Map;

/**
 * 奖励vo
 * @author lxn
 *
 */
public class HuoYueDuAwardConfig{

	private Integer id; //奖励id
	private Integer jifen; //对应积分
	private Map<String, Integer> items; //奖励
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getJifen() {
		return jifen;
	}
	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}
	public Map<String, Integer> getItems() {
		return items;
	}
	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}
	
}
