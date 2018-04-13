package com.junyou.bus.qiling.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.vo.QiLingRankVo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class QiLingInfoDao extends BusAbsCacheDao<QiLingInfo> implements IDaoOperation<QiLingInfo> {
	
	public QiLingInfo initQiLingInfo(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}
 
	public QiLingInfo dbLoadQiLingInfo(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<QiLingInfo> list =  getRecords(queryParams);
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<QiLingRankVo> getQiLingRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectQiLingRankToQiLingRankVo", queryParams);
	
	}
}