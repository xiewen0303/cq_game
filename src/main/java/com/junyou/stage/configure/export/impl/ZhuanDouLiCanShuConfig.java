package com.junyou.stage.configure.export.impl;

import java.util.HashMap;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 战斗力系数配置表 
 *
 * @author DaoZheng Yuan
 * @date 2013-10-28 10:54:27
 */
public class ZhuanDouLiCanShuConfig extends AbsVersion{

	private String id;
	
	private Map<String,Float> attrXs;
		
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Float> getAttrXs() {
		return attrXs;
	}

	public void addAttrXs(String name, Float value) {
		
		if(attrXs == null){
			attrXs = new HashMap<String, Float>();
		}
		
		attrXs.put(name, value);
	}

}
