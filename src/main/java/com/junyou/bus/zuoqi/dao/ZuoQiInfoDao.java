package com.junyou.bus.zuoqi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.vo.ZuoqiRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class ZuoQiInfoDao extends BusAbsCacheDao<ZuoQiInfo> implements IDaoOperation<ZuoQiInfo> {
	
	public ZuoQiInfo initZuoQiInfo(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}

	@SuppressWarnings("unchecked")
	public List<ZuoqiRankVo> getZuoqiRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectZuoqiRankToZuoqiRankVo", queryParams);
	
	}
	/**
	 * 获取坐骑等级大于level的所有玩家ID
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRoleIdByLevel(int level) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("level", level);
		return query("selectByParamsZuoQiLevel", queryParams);
		
	}
	
	public ZuoQiInfo dbLoadZuoQi(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<ZuoQiInfo> list =  getRecords(queryParams);
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}
}