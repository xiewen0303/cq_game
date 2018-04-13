package com.junyou.bus.friend.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository; 
import com.junyou.bus.friend.entity.Friend;
import com.junyou.bus.friend.vo.FriendVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.constants.GameConstants; 
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

/**
 * 好友Dao
 * @description 
 * @author Hanchun
 * @email han88316250@163.com
 * @date 2015-1-7 下午6:01:38
 */
@Repository
public class FriendDao extends BusAbsCacheDao<Friend> implements IDaoOperation<Friend> {

//	public List<Friend> initFriend(Long userRoleId) {
//		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
//		queryParams.put("userRoleId", userRoleId);
//		
//		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
//	}
	
	@SuppressWarnings("unchecked")
	public List<FriendVo> getFriendsByParams(String params){
		if(params == null || "".equals(params)){
			return null;
		}
		
		String roleList =  params.substring(0,params.lastIndexOf(GameConstants.F_SPLIT_CHAR));
		 
		
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("roleList", roleList);
//		queryParams.put("isDel", GameConstants.NOT_IS_DEL);
		
		return query("selectFriendsVoByParams", queryParams);
	}
	
	@SuppressWarnings("unchecked")
	public FriendVo getFriendVoByParams(String roleName){
		
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("roleName", roleName);
//		queryParams.put("isDel", GameConstants.NOT_IS_DEL);
		 
		 List<FriendVo> friendVos = query("selectFriendVoByParams", queryParams);
		 if(friendVos!=null && friendVos.size()>0){
			 return friendVos.get(0);
		 }
		 
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public FriendVo getFriendVoByParams(long userRoleId){
		
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("roleList", userRoleId);
//		queryParams.put("isDel", GameConstants.NOT_IS_DEL);
		
		List<FriendVo> friendVos =query("selectFriendsVoByParams", queryParams); 
		  
		 if(friendVos!=null && friendVos.size()>0){
			 return friendVos.get(0);
		 }
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<FriendVo> getFriendsByParams(Map<Long, Integer> params) {
		if(params == null || params.size() < 1){
			return null;
		}
		
		StringBuffer roleListSB =new StringBuffer();
		for (Long userRoleId : params.keySet()) {
			roleListSB.append(userRoleId).append(GameConstants.F_SPLIT_CHAR);
		}
		String roleList =  roleListSB.substring(0, roleListSB.lastIndexOf(GameConstants.F_SPLIT_CHAR));
		
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("roleList", roleList);
//		queryParams.put("isDel", GameConstants.NOT_IS_DEL); 
		
		return query("selectFriendsVoByParams", queryParams);
	}
}