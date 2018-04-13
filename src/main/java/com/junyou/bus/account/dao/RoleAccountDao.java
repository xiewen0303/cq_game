package com.junyou.bus.account.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

/**
 * 玩家角色货币Dao
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-11-28 下午4:12:07
 */
@Repository
public class RoleAccountDao extends BusAbsCacheDao<RoleAccount> implements IDaoOperation<RoleAccount> {

	/**
	 * 初始化玩家角色货币信息直接访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleAccount initRoleAccountFromDb(Long userRoleId) { 
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<RoleAccount> list = getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 获取玩家角色货币信息直接访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleAccount getRoleAccountFromDb(String userId,String serverId) {
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userId", userId);
		queryParams.put("serverId", serverId);
		
		List<RoleAccount> list = getRecords(queryParams, null, AccessType.getDirectDbType());
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 修改玩家角色货币信息直接访问库
	 * @param account
	 */
	public void updateRoleAccountFromDb(RoleAccount account) {
		update(account, null, AccessType.getDirectDbType());
	}
	
	/**
	 * 创建玩家角色货币信息直接访问库
	 * @param roleAccount
	 */
	public void insertRoleAccountFromDb(RoleAccount roleAccount){
		insert(roleAccount, roleAccount.getUserRoleId(), AccessType.getDirectDbType());
	}
}