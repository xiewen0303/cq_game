package com.junyou.bus.wuqi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.vo.WuQiRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class WuQiInfoDao extends BusAbsCacheDao<WuQiInfo> implements IDaoOperation<WuQiInfo> {
	
	public WuQiInfo initwuqiInfo(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}

	@SuppressWarnings("unchecked")
	public List<WuQiRankVo> getWuQiRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectWuQiRankToWuQiRankVo", queryParams);
	
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
		return query("selectByParamsWuQiLevel", queryParams);
		
	}
	
	public WuQiInfo dbLoadwuqi(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<WuQiInfo> list =  getRecords(queryParams);
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}
}