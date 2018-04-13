package com.junyou.bus.lunpan.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *@Description 充值轮盘物品
 *@Author Yang Gao
 *@Since 2016-6-4
 *@Version 1.1.0
 */
public class LunPanUtils {

	/**
	 * Map<子活动ID(subId),Map<角色ID, Map<String, ShenMiShangDianConfig>>>
	 */
	private static Map<Integer,Map<Long, Map<Integer, Object[]>>> xyzpMap = new HashMap<>();
	
    public static void setXYZPMap(Long userRoleId, Map<Integer, Object[]> map,Integer subId){
		Map<Long,  Map<Integer, Object[]>> uMap = xyzpMap.get(subId);
		if(uMap == null){
			uMap = new HashMap<>();
		}
		uMap.put(userRoleId, map);
		xyzpMap.put(subId, uMap);
		
	}
	
	public static Map<Integer, Object[]> getXYZPMap(Long userRoleId,Integer subId){
		if(xyzpMap.get(subId) == null){
			return null;
		}
		return xyzpMap.get(subId).get(userRoleId);
	}
	
	public static void removeXyzpMap(String userRoleId,Integer subId){
		if(xyzpMap.get(subId) == null){
			return;
		}
		xyzpMap.remove(userRoleId);
	}

}
