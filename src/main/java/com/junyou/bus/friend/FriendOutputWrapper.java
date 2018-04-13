package com.junyou.bus.friend;

import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.err.AppErrorCode;
 
/**
 * 好友输出
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-28 下午4:17:40
 */
public class FriendOutputWrapper {
	
//	public static Object[] getFriendsData(List<FriendVo> resultList, Integer type){
//		Object[] friendDatas = null;
//		if(resultList != null){
//			friendDatas = new Object[resultList.size()];
//			
//			int index = 0;
//			for (FriendVo friendVo : resultList) {
//				if(friendVo != null){
//					friendDatas[index++] = getSingleFriend(friendVo);
//				}
//			}
//		}
//		
//		return new Object[]{1,type, friendDatas};
//	}
	
//	public static Object[] getSingleFriend(FriendVo friendVo,int type){
//		    
//		
//			return new Object[]{
//					friendVo.getConfigId(),
//					friendVo.getId(),
//					friendVo.getName(),
//					friendVo.getLevel(),
//					 
//					friendVo.getState()
//			};
//	}
	
	private static Object[] getFriendInfoByRole(RoleWrapper loginRole,boolean isOnline, Object finalEnemyIds){
		if(finalEnemyIds != null ){
			finalEnemyIds = Long.valueOf(finalEnemyIds.toString());
		}
			return new Object[]{new Object[]{loginRole.getConfigId(),loginRole.getId(),loginRole.getName(),loginRole.getLevel(),isOnline},finalEnemyIds};
	}
	public static Object[] add(RoleWrapper loginRole,boolean isOnline){
		return new Object[]{AppErrorCode.SUCCESS,new Object[]{loginRole.getConfigId(),loginRole.getId(),loginRole.getName(),loginRole.getLevel(),isOnline}};
	}
	
	public static Object[] addEnemy(RoleWrapper loginRole,boolean isOnline ,Object finalEnemyIds){
		return getFriendInfoByRole(loginRole, isOnline, finalEnemyIds);
	}
	
	public static Object[] del(Long targetRoleId,int type){
		return new Object[]{AppErrorCode.SUCCESS,type,targetRoleId};
	}
}
