package com.junyou.bus.platform.qq.confiure.export;

import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 * TGP特权等级礼包
 * @author Administrator
 *
 */
public class QqTgpchengzhangPublicConfig extends AdapterPublicConfig {
	private Map<Integer, Integer> levels;//MAP<ID,等级>

	private Map<Integer, Map<String, Integer>> items;//MAP<等级,MAP<物品ID,物品数量>>

	public Map<Integer, Integer> getLevels() {
		return levels;
	}

	public void setLevels(Map<Integer, Integer> levels) {
		this.levels = levels;
	}

	public Map<Integer, Map<String, Integer>> getItems() {
		return items;
	}

	public void setItems(Map<Integer, Map<String, Integer>> items) {
		this.items = items;
	}
} 
