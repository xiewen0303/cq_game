package com.junyou.public_.guild.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.public_.guild.entity.GuildApply;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class GuildApplyDao extends AbsDao<GuildApply> implements IDaoOperation<GuildApply> {

	public List<GuildApply> initGuildApply(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	/**
	 * 查询该角色在此公会的申请记录
	 * @param userRoleId
	 * @param guildId
	 * @return
	 */
	public GuildApply getRoleGuildApply(Long userRoleId,Long guildId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		queryParams.put("guildId", guildId);
		List<GuildApply> list = getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
		if(list == null || list.size() < 1){
			return null;
		}
		return list.get(0);
	}
	/**
	 * 获取公会所有申请列表
	 * @param guildId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GuildApply> getGuildApplyList(Long guildId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("guildId", guildId);
		return query("selectMultiGuildMemberVo", queryParams);
	}
	
	/**
	 * 删除该玩家所有申请记录
	 * @param userRoleId
	 */
	public void delAllApply(Long userRoleId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		delete("deleteAllGuildApplysByRole", queryParams);
	}
	/**
	 * 批量删除该公会指定玩家的申请记录
	 * @param userRoleIds
	 * @param guildId
	 */
	public void delGuildApply(String userRoleIds,Long guildId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleIds", userRoleIds);
		queryParams.put("guildId", guildId);
		delete("deleteGuildApplys", queryParams);
	}
}