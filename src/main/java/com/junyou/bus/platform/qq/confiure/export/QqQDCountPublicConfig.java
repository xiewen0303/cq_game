package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class QqQDCountPublicConfig extends AdapterPublicConfig {
	private Map<String, Integer> items;

	public Map<String, Integer> getItems() {
		return items;
	}

	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}

	
}
