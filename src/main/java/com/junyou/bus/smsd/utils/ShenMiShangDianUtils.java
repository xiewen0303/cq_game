package com.junyou.bus.smsd.utils;

import java.util.HashMap;
import java.util.Map;

import com.junyou.bus.smsd.configue.export.ShenMiShangDianConfig;

public class ShenMiShangDianUtils {

	/**
	 * Map<子活动ID(subId),Map<角色ID, Map<String, ShenMiShangDianConfig>>>
	 */
	private static Map<Integer,Map<Long, Map<Integer, ShenMiShangDianConfig>>> smsdMap = new HashMap<>();
	
	public static void setSMSDMap(Long userRoleId, Map<Integer, ShenMiShangDianConfig> map,Integer subId){
		Map<Long, Map<Integer, ShenMiShangDianConfig>> uMap = smsdMap.get(subId);
		if(uMap == null){
			uMap = new HashMap<>();
		}
		uMap.put(userRoleId, map);
		smsdMap.put(subId, uMap);
	}
	
	public static Map<Integer, ShenMiShangDianConfig> getSMSDMap(Long userRoleId,Integer subId){
		if(smsdMap.get(subId) == null){
			return null;
		}
		return smsdMap.get(subId).get(userRoleId);
	}
	
	
	private static Map<Long, Integer> userBanBen = new HashMap<>();
	
	public static void setUserBanBen(Long userRoleId, Integer banben){
		userBanBen.remove(userRoleId);
		userBanBen.put(userRoleId, banben);
	}
	
	public static Integer getUserBanBen(Long userRoleId){
		if(userBanBen.get(userRoleId) == null){
			return 0;
		}
		return userBanBen.get(userRoleId);
	}
	
}
