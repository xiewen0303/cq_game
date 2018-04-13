package com.junyou.bus.friend.util;

import java.util.Map;

import com.junyou.constants.GameConstants;

/**
 *  好友
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-3-2 下午5:04:47
 */
public class FriendUtil {

	public static String addMember(Long friendId,String friendEntityIds){
		if(friendEntityIds == null){
			return friendId+GameConstants.F_SPLIT_CHAR;
		}
		return friendEntityIds+friendId+GameConstants.F_SPLIT_CHAR;
	}

	public static String delMember(Long friendId,String friendEntityIds){
		
		if(friendEntityIds == null || "".equals(friendEntityIds)){
			return "";
		}else{
			return friendEntityIds.replaceAll(friendId+GameConstants.F_SPLIT_CHAR, "");
		}
	}
	
	public static Map<Long, Integer> delMember(Long targetRoleId, Map<Long, Integer> enemyDatas) {
		enemyDatas.remove(targetRoleId);
		return enemyDatas;
	}
	
	public static Map<Long, Integer> addMember(Long targetRoleId, Map<Long, Integer> enemyDatas) {
		if(enemyDatas != null){
			enemyDatas.remove(targetRoleId);
		}
		return enemyDatas;
	}
	
	
	public static int getMemberCount(String friendEntityIds){
		if(friendEntityIds == null || friendEntityIds.equals("")){
			return 0;
		}else{
			return friendEntityIds.split(GameConstants.F_SPLIT_CHAR).length;
		}
	}
	
	
	
	/**
	 * 是否已包含
	 * @param friendId
	 * @param friendEntityIds
	 * @return true:包含  false:不包含
	 */
	public static boolean isContainsFriendId(Long friendId,String friendEntityIds){
		if(friendEntityIds != null && friendEntityIds.contains(String.valueOf(friendId))){
			return true;
		}else{
			return false;
		}
	} 
			
}