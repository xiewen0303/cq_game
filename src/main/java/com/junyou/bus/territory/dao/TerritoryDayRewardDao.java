package com.junyou.bus.territory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.territory.entity.TerritoryDayReward;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class TerritoryDayRewardDao extends BusAbsCacheDao<TerritoryDayReward> implements IDaoOperation<TerritoryDayReward> {

	public List<TerritoryDayReward> initTerritoryDayReward(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}