package com.junyou.bus.role.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.vo.LevelRankVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.accessor.GlobalIdentity;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

/**
 * 玩家角色Dao
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:53:20
 */
@Repository
public class UserRoleDao extends BusAbsCacheDao<UserRole> implements IDaoOperation<UserRole> {

	/**
	 * 获取玩家信息直接访问库
	 * @param userRoleId
	 * @return
	 */
	public UserRole initUserRoleFromDb(Long userRoleId) {
		return load(userRoleId, userRoleId, AccessType.getDirectDbType());
	}
	
	/**
	 * 创建玩家信息直接入库
	 * @param userRole
	 */
	public void insertUserRoleFromDb(UserRole userRole) {
		insert(userRole, userRole.getId(), AccessType.getDirectDbType());
	}
	
	/**
	 * 修改玩家信息直接访问库
	 * @param userRole
	 */
	public void updateUserRoleFromDb(UserRole userRole) {
		update(userRole, userRole.getId(), AccessType.getDirectDbType());
	}
	
	/**
	 * 获取为该名称的size直接访问库
	 * @param roleName（不包括模糊查询）
	 * @return
	 */
	public int getRolesFromDb(String roleName){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("name", roleName);
		
		List<UserRole> list = getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
		return list == null ? 0 : list.size();
	}
	
	/**
	 * 获取玩家该角色信息
	 * @param userRoleId
	 * @param isDel（0查未删除，1查删除）
	 * @return
	 */
	public UserRole getUserRoleFromDb(Long userRoleId, int isDel){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		
		queryParams.put("id", userRoleId);
		queryParams.put("isDel", isDel);
		
		List<UserRole> list = getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * 获取该玩家在该服务器所有的信息直接访问库
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public List<UserRole> getRolesFromDb(String userId, String serverId) {
		
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userId", userId);
		queryParams.put("serverId", serverId);
		
		return getRecords(queryParams, GlobalIdentity.get(), AccessType.getDirectDbType());
	}
	
	
	@SuppressWarnings("unchecked")
	public List<LevelRankVo> getLevelRankVo(int limit){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectLevelRankToLevelRankVo", queryParams);
	}
	
	/**
	 * pps单服激活玩家数据 (获取各个服务器24小时内的新创角人数，新创角的角色信息列表)
	 * @param serverId
	 * @param dateStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPPsJHRoles(String serverId,String dateStr){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("serverId", serverId);
		queryParams.put("createTime", dateStr);
		
		return query("selectPpsJHRole", queryParams);
	}
	
	/**
	 * pps单服活跃玩家数据 (获取各个服务器24小时内的进入游戏游玩的有效用户数)
	 * @param serverId
	 * @param dateStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPPsHYRoles(String serverId,String dateStr){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("serverId", serverId);
		queryParams.put("hyTime", dateStr);
		
		return query("selectPpsHYRole", queryParams);
	}
	
	public boolean isNameExist(String name){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("name", name);
		List<Long> list = query("countByName", queryParams);
		if(list == null || list.size() == 0){
			return false;
		}
		if(list.get(0) == null){
			return false;
		}
		return list.get(0).intValue()>0;
	}
	/**
	 * 查询角色在全服的等级排名
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public long getUserServerRank(String userId,String serverId){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userId", userId);
		queryParams.put("serverId", serverId);
		List<Long> list = query("getUserServerLeveRank", queryParams);
		if(list!= null && list.size() > 0){
			return list.get(0);
		}
		return 0;
	}

	/**
	 * 获取总注册人数
	 * @return
	 */
	public int getTotalRegistCount(){
		List<Integer> list = query("allRegistCount", null);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.get(0);
	}
	
	/**
	 * 获取时间区间内注册人数
	 * @return
	 */
	public int getTimeRegistCount(long startTime,long endTime){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("startTime", startTime);
		queryParams.put("endTime", endTime);
		List<Integer> list = query("selectHourCreateRoleCount", queryParams);
		if(list == null || list.size() == 0){
			return 0;
		}
		return list.get(0);
	}
}