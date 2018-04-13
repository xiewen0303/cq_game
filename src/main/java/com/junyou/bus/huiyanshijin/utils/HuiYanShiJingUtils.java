package com.junyou.bus.huiyanshijin.utils;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.datetime.GameSystemTime;

/**
 * @author zhongdian
 * 2016-3-23 下午3:43:50
 * 慧眼识金工具类
 */
public class HuiYanShiJingUtils {

	
	private static Map<Integer,Map<Integer, Integer>> countMap = new HashMap<>();//MAP<子活动ID,MAP<挖矿级别,次数>>
	private static Map<Integer,Long> updateTimeMap= new HashMap<>();//MAP<子活动ID,更新时间>

	public static void addCountBySubId(Integer subId,Integer level){
		Map<Integer, Integer>  map = countMap.get(subId);
		if(map == null){
			map = new HashMap<>();
		}
		if(map.get(level) == null){
			map.put(level, 1);
		}else{
			map.put(level, map.get(level).intValue()+1);
		}
		countMap.put(subId, map);
		updateTimeMap.put(subId, GameSystemTime.getSystemMillTime());
	}
	
	public static int getCountBySubId(Integer subId,Integer level){
		Map<Integer, Integer>  map = countMap.get(subId);
		if(map == null){
			return 0;
		}
		if(map.get(level) == null){
			return 0;
		}else{
			return map.get(level);
		}
	}
	
	public static long getUpdateTime(Integer subId){
		if(updateTimeMap.get(subId) == null){
			return 0;
		}
		return  updateTimeMap.get(subId);
	}
	
	public static void cleanMap(Integer subId){
		Map<Integer, Integer>  map = countMap.get(subId);
		if(map != null){
			map = new HashMap<>();
		}
		updateTimeMap.put(subId, GameSystemTime.getSystemMillTime());
	}
	
}
