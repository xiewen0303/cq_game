package com.junyou.bus.platform.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/**
 *  礼包公共数据表
 * @author lxn
 * @date 2015-6-9
 */
public class PtCommonPublicConfig extends AdapterPublicConfig {
	
	/**
	 * 奖励礼包.{key:{itemId:num,...}}
	 * 1等级礼包.{level1:{itemId:num,...},level2:{itemId:num,...},...}
	 * 2.没等级.{"item":{itemId:num,...}} 
	 */
	private  Map<String, Map<String, Integer>> items; //

	private  Map<String, Object> info; //
	
	public Map<String, Object> getInfo() {
		return info;
	}
	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}
	 
	public Map<String, Map<String, Integer>> getItems() {
		return items;
	}
	public void setItems(String levelStr, Map<String, Integer> awardMap) {
		if(this.items==null){
			this.items = new HashMap<String, Map<String,Integer>>();	
		}
		this.items.put(levelStr, awardMap);
	}

}
