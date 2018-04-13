package com.junyou.bus.tianyu.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class TianYuInfoDao extends BusAbsCacheDao<TianYuInfo> implements IDaoOperation<TianYuInfo> {

	public TianYuInfo initTianYuInfo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	public TianYuInfo dbLoadTianYuInfo(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		List<TianYuInfo> list =  getRecords(queryParams);
		if(list != null && list.size() >0){
			return list.get(0);
		}
		return null;
	}
	
}