package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.Map;


/**
 * 随机属性（公共配置中）
 * @description 
 * @author Hanchun
 * @date 2015-1-6 下午8:21:43
 */
public class RandomAttrPublicConfig extends AdapterPublicConfig{
	
	private Map<Integer,Integer> data = null;

	public Map<Integer, Integer> getData() {
		return data;
	}

	public void setData(Map<Integer, Integer> data) {
		this.data = data;
	}

	public Integer getRandomCount(int rareLevel) {
		return data.get(rareLevel);
	} 
}
