package com.junyou.bus.chengshen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.chengshen.entity.RoleChengShen;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleChengShenDao extends BusAbsCacheDao<RoleChengShen> implements IDaoOperation<RoleChengShen> {

	public List<RoleChengShen> initRoleChengShen(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}