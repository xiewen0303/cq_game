package com.junyou.bus.platform.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

public class _360VplanAttrConfig extends AdapterPublicConfig{

	private  Map<String, Map<String, Long>> attrMap; //属性
	
	public Map<String, Map<String, Long>> getAttrMap() {
		return attrMap;
	}
	public void addAttrMap(String key, Map<String, Long> attrMap) {
		if(this.attrMap==null){
			this.attrMap = new HashMap<>();
		}
		this.attrMap.put(key, attrMap);
		
	}
	 
}
