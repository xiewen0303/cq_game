package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @description 阵营
 *
 * @author LiuJuan
 * @date 2012-3-12 下午3:56:34
 */
public class EnemyCampsConfig{

	private Integer id;
	
	// key : guanxi id val: guanxi
	private Map<Integer,Integer> guanXiMap = new HashMap<>();

	public void add(Integer i, Integer guanXi) {
		guanXiMap.put(i, guanXi);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<Integer, Integer> getGuanXiMap() {
		return guanXiMap;
	}
}
