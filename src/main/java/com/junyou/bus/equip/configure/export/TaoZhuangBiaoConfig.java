package com.junyou.bus.equip.configure.export;

import java.util.Map;

import com.junyou.utils.collection.ReadOnlyMap;


/**
 * 套装表 
 * @author wind
 * @date 2015-01-13 20:30:59
 */
public class TaoZhuangBiaoConfig {

	private int id;

	private int count;//套装1的数量

	private Map<String,Long> attrs;//套装所有属性 
	 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = new ReadOnlyMap<>(attrs);
	}
}
