package com.junyou.bus.zhuanpan.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 幸运转盘物品
 * @author ZHONGDIAN
 *
 */
public class ZhuanPanUtils {

	/**
	 * integer 配置ID
	 */
	/*private static Map<String, Map<String, Object[]>> xyzpMap = new HashMap<String,  Map<String, Object[]>>();
	
	public static void setXYZPMap(String userRoleId, Map<String, Object[]> map){
		xyzpMap.remove(userRoleId);
		xyzpMap.put(userRoleId, map);
	}
	
	public static Map<String, Object[]> getXYZPMap(String userRoleId){
		return xyzpMap.get(userRoleId);
	}
	public static void removeXyzpMap(String userRoleId){
		xyzpMap.remove(userRoleId);
	}
*/
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
