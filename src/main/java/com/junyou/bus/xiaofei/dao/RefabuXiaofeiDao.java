package com.junyou.bus.xiaofei.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xiaofei.entity.RefabuXiaofei;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuXiaofeiDao extends BusAbsCacheDao<RefabuXiaofei> implements IDaoOperation<RefabuXiaofei> {

	public List<RefabuXiaofei> initRefabuXiaofei(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RefabuXiaofei> dbLoadAllByRank(int subId,int maxSize) {
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String,Object>();
		queryParams.put("maxSize", maxSize);
		queryParams.put("subId", subId);
		
		return query("selectXiaoFeiRank", queryParams);
	}
	/**
	 * 根据subId 删除活动数据
	 * @param subId
	 * @param maxSize
	 * @return
	 */
	public void dbDeleteBySubId(int subId) {
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String,Object>();
		queryParams.put("subId", subId);
		
		delete("deleteRefabuXiaofeiBySubId", queryParams);
	}

}