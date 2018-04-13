package com.junyou.bus.platform.qq.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhongdian
 * 2015-9-11 上午10:18:23
 */
public class QQGeZhongZuanMap {

	private static Map<Long, Map<Integer, Object>>  zuanMap = new HashMap<Long, Map<Integer,Object>>();
	
	//蓝钻到期时间
	private static Map<Long, Long> daoQiTimeMap = new HashMap<>();
	


	public static void setUserZuanMap(Long userRoleId,Map<Integer, Object> map){
		zuanMap.put(userRoleId, map);
	}
	
	
	public static Map<Integer, Object> getZuanMapByUser(Long userRoleId){
		return zuanMap.get(userRoleId);
	}
	
	public static void setDaoQiTime(Long userRoleId,Long time){
		daoQiTimeMap.put(userRoleId, time);
	}
	
	
	public static Long getDaoQiTime(Long userRoleId){
		return daoQiTimeMap.get(userRoleId);
	}
	
	public static void reZuanMapByUser(Long userRoleId){
		if(zuanMap == null ||zuanMap.size() <=0){
			return;
		}
		zuanMap.remove(userRoleId);
	}
}
