package com.junyou.bus.platform.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.common.entity.RolePlatformInfo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RolePlatformInfoDao extends BusAbsCacheDao<RolePlatformInfo> implements IDaoOperation<RolePlatformInfo> {

	public List<RolePlatformInfo> initRolePlatformInfo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}