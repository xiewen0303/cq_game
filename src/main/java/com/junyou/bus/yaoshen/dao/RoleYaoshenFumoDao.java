package com.junyou.bus.yaoshen.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshenFumo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleYaoshenFumoDao extends BusAbsCacheDao<RoleYaoshenFumo> implements IDaoOperation<RoleYaoshenFumo> {

	public List<RoleYaoshenFumo> initRoleYaoshenFumo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}