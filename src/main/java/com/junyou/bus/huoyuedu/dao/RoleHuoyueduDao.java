package com.junyou.bus.huoyuedu.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.huoyuedu.entity.RoleHuoyuedu;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleHuoyueduDao extends BusAbsCacheDao<RoleHuoyuedu> implements IDaoOperation<RoleHuoyuedu> {

	public List<RoleHuoyuedu> initRoleHuoyuedu(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}