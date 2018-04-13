package com.junyou.bus.platform.yuenan.confiure.export;

import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class YueNanYaoQingPublicConfig extends AdapterPublicConfig {

	private Map<String, Integer> itemMap;//奖励物品
	
	private Integer yaoqingCount;//邀请几个好友可以领一次奖
		
	private Integer lingQuCount;//每天可以领取次数

	public Map<String, Integer> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Integer> itemMap) {
		this.itemMap = itemMap;
	}

	public Integer getYaoqingCount() {
		return yaoqingCount;
	}

	public void setYaoqingCount(Integer yaoqingCount) {
		this.yaoqingCount = yaoqingCount;
	}

	public Integer getLingQuCount() {
		return lingQuCount;
	}

	public void setLingQuCount(Integer lingQuCount) {
		this.lingQuCount = lingQuCount;
	}
	
}
