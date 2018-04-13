package com.junyou.bus.chibang.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.vo.ChiBangRankVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class ChiBangInfoDao extends BusAbsCacheDao<ChiBangInfo> implements IDaoOperation<ChiBangInfo> {
	
	public ChiBangInfo initChiBangInfo(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}
 
	public ChiBangInfo dbLoadChiBangInfo(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<ChiBangInfo> list =  getRecords(queryParams);
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
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
		return query("selectByParamsChiBangLevel", queryParams);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ChiBangRankVo> getChiBangRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectChiBangRankToChiBangRankVo", queryParams);
	
	}
}