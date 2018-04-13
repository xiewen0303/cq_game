package com.junyou.bus.platform.qq.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.qq.entity.RolePlatformQqLz;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RolePlatformQqLzDao extends BusAbsCacheDao<RolePlatformQqLz> implements IDaoOperation<RolePlatformQqLz> {

	public List<RolePlatformQqLz> initRolePlatformQqLz(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}