package com.junyou.bus.onlinerewards.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.onlinerewards.entity.RoleOnlineRewards;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleOnlineRewardsDao extends BusAbsCacheDao<RoleOnlineRewards> implements IDaoOperation<RoleOnlineRewards> {

	public List<RoleOnlineRewards> initRoleOnlineRewards(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}