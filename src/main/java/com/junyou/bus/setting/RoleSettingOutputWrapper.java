package com.junyou.bus.setting;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 角色设置（快捷键等）
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-16 下午6:49:16 
 */
public class RoleSettingOutputWrapper {
	private static final String TYPE_KEY = "type";
	private static final String DATA_KEY = "data";
	
	/**
	 * 设置快捷键
	 * @param quickbarInfo
	 * @param index
	 * @param type
	 * @param data
	 * @return
	 */
	public static String setQuickbarInfo(Map<String, Map<String, Object>> qbInfoMap, String index, Integer type, String data){
		if(qbInfoMap == null){
			qbInfoMap = new HashMap<>();
		}
		
		Map<String, Object> map = qbInfoMap.get(index.toString());
		if(map == null || map.size() <= 0){
			map = new HashMap<>();
		}
		map.put(TYPE_KEY, type);
		map.put(DATA_KEY, data);
		qbInfoMap.put(index, map);
		
		return JSON.toJSONString(qbInfoMap);
	}
	
	/**
	 * Map转换为String（快捷键使用）
	 * @param qbInfoMap
	 * @return
	 */
	public static String map2StrQuickbarInfo(Map<String, Map<String, Object>> qbInfoMap){
		if(qbInfoMap == null || qbInfoMap.size() <= 0){
			return "";
		}
		return JSON.toJSONString(qbInfoMap);
	}

	/**
	 * Map转换为Object（快捷键使用）
	 * @param qbInfoMap
	 * @return [[0:int(快捷栏索引号),1:int(类型),2:String(数据)],[0:int(快捷栏索引号),1:int(类型),2:String(数据)]...]
	 */
	public static Object[] map2ObjQuickbarInfo(Map<String, Map<String, Object>> qbInfoMap){
		if(qbInfoMap == null || qbInfoMap.size() <= 0){
			return null;
		}
		
		Object[] data = new Object[qbInfoMap.size()];
		int i = 0;
		
		for(Map.Entry<String, Map<String, Object>> entry : qbInfoMap.entrySet()){
			Map<String, Object> map = entry.getValue();
			data[i++] = new Object[]{entry.getKey(), map.get(TYPE_KEY), map.get(DATA_KEY)};
		}
		return data;
	}
}