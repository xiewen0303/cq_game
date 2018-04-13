package com.junyou.bus.linghuo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.linghuo.entity.RoleLinghuoInfo;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleLinghuoInfoDao extends BusAbsCacheDao<RoleLinghuoInfo> implements IDaoOperation<RoleLinghuoInfo> {

	public List<RoleLinghuoInfo> initRoleLinghuoInfo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}