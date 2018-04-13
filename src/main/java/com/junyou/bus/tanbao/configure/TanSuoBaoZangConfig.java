package com.junyou.bus.tanbao.configure;

import java.util.Map;


/**
 * 
 * @description 热发布活动公共配置表 
 *
 * @author ZHONGDIAN
 * @date 2014-04-23 15:22:05
 */
public class TanSuoBaoZangConfig {

	private Integer id;
	
	private Float jilv;//开启下一层的几率
	
	private Map<String,Integer> itemMap; 
	
	private Integer gold; //探索需要消耗的元宝
	
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Float getJilv() {
		return jilv;
	}

	public void setJilv(Float jilv) {
		this.jilv = jilv;
	}

	public Map<String,Integer> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String,Integer> itemMap) {
		this.itemMap = itemMap;
	}
	

}
