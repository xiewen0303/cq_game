package com.junyou.bus.lj.dao;

import org.springframework.stereotype.Repository;

import com.junyou.bus.lj.entity.RoleLj;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleLjDao extends BusAbsCacheDao<RoleLj> implements IDaoOperation<RoleLj> {

	public RoleLj initRoleLjInfo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		return load(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}