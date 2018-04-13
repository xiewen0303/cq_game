package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class QqLZdengjiPublicConfig extends AdapterPublicConfig {
	private Map<Integer, Map<String, Integer>> items;

	public Map<Integer, Map<String, Integer>> getItems() {
		return items;
	}

	public void setItems(Map<Integer, Map<String, Integer>> items) {
		this.items = items;
	}

	public Map<String,Integer> getItemsByLevel(Integer level){
		return items.get(level);
	}
	
}
