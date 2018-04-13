package com.junyou.bus.kuafu_qunxianyan.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongdian
 * 2016-4-11 上午10:14:54
 */
public class KuafuQunXianYanUtils {

	private static Map<Long, Integer> jMap = new HashMap<Long, Integer>();
	
	public static boolean getJmapByUserRoleId(Long userRoleId){
		Integer status = jMap.get(userRoleId);
		if(status != null){
			return true;
		}
		return false;
	}
	
	public static void serJmapByUserRoleId(long userRoleId){
		jMap.put(userRoleId, 1);
	}
	
	public static void clearJmap(){
		jMap.clear();
	}
}
