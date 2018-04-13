package com.junyou.stage.manage;

import java.util.HashMap;
import java.util.Map;

import com.junyou.utils.datetime.GameSystemTime;

public class RoleInfoManager {
	private static long expireTime = 0;
	private static final long CLEAR_TIME = 3600000l;
	private static Map<String,Object> expireRoleInfoMap;
	private static Map<String,Object> nowRoleInfoMap;
	
	private static void init(){
		expireTime = GameSystemTime.getSystemMillTime() + CLEAR_TIME;
		expireRoleInfoMap = new HashMap<String, Object>();
		nowRoleInfoMap = new HashMap<String, Object>();
	}
	
	public static void put(String userRoleId,Object roleInfo){
		if(expireTime == 0){
			init();
		}
		
		expireOperation();
		
		nowRoleInfoMap.put(userRoleId, roleInfo);
	}
	
	public static Object get(String userRoleId){
		if(expireTime == 0){
			init();
		}
		
		Object roleInfo = nowRoleInfoMap.get(userRoleId);
		if(roleInfo == null){
			roleInfo = expireRoleInfoMap.get(userRoleId);
			if(roleInfo != null){
				nowRoleInfoMap.put(userRoleId, roleInfo);
			}
		}
		
		expireOperation();
		
		return roleInfo;
	}
	
	/**
	 * 超时验证并处理
	 */
	private static void expireOperation(){
		long cur = GameSystemTime.getSystemMillTime();
		if(cur > expireTime){
			expireTime = cur + CLEAR_TIME;
			expireRoleInfoMap = nowRoleInfoMap;
			nowRoleInfoMap = new HashMap<String, Object>();
		}
	}
}
