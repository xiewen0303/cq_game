package com.junyou.bus.hczbs.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.hczbs.entity.ZhengbasaiDayReward;
import com.junyou.public_.share.dao.PublicAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class ZhengbasaiDayRewardDao extends PublicAbsCacheDao<ZhengbasaiDayReward> implements IDaoOperation<ZhengbasaiDayReward> {

	public List<ZhengbasaiDayReward> initZhengbasaiDayReward(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	public List<ZhengbasaiDayReward> loadAll() {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	/**
	 * 清空表数据
	 * @param userRoleId
	 */
	public void delAllData(){
		delete("truncateZhengbasaiDayReward", null);
	}
}