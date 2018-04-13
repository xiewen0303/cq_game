package com.junyou.bus.chengjiu.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.chengjiu.entity.RoleChengjiuData;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleChengjiuDataDao extends BusAbsCacheDao<RoleChengjiuData> implements IDaoOperation<RoleChengjiuData> {

	public List<RoleChengjiuData> initRoleChengjiuData(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}