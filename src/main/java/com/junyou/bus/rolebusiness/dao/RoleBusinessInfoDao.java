package com.junyou.bus.rolebusiness.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.role.vo.LevelRankVo;
import com.junyou.bus.rolebusiness.entity.RoleBusinessInfo;
import com.junyou.bus.rolebusiness.vo.FightingRankVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.constants.GameConstants;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

/**
 * 业务数据
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-3 下午4:19:51
 */
@Repository
public class RoleBusinessInfoDao extends BusAbsCacheDao<RoleBusinessInfo> implements IDaoOperation<RoleBusinessInfo> {
	@SuppressWarnings("unchecked")
	public List<Object> getZhanLiRanks(){
		int maxSize = GameConstants.MAX_RANK_SIZE;
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String,Object>();
		queryParams.put("maxSize", maxSize);
		
		return query("selectZhangLiRank", queryParams);
	}
	/**
	 * 获取该玩家的业务数据直接访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleBusinessInfo getRoleBusinessInfoForDB(Long userRoleId){
		return load(userRoleId, userRoleId, AccessType.getDirectDbType());
	}
	
	@SuppressWarnings("unchecked")
	public  List<FightingRankVo>  getFightingRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectFightingRankToFightingRankVo", queryParams);
		
	}
	
	/**
	 * 获取等级大于level的所有玩家ID
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRoleIdByLevel(int level) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("level", level);
		return query("selectByParamsFighter", queryParams);
		
	}
	
	/**
	 * 查询角色在全服的战力排名
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public long getUserServerRank(String userId,String serverId){
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userId", userId);
		queryParams.put("serverId", serverId);
		List<Long> list = query("getUserServerFighterRank", queryParams);
		if(list!= null && list.size() > 0){
			return list.get(0);
		}
		return 0;
	}
}