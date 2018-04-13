package com.junyou.bus.xianqi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xianqi.entity.RoleXianyuanFeihua;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleXianyuanFeihuaDao extends BusAbsCacheDao<RoleXianyuanFeihua> implements IDaoOperation<RoleXianyuanFeihua>  {

	public List<RoleXianyuanFeihua> dbLoadRoleXianyuanFeihua(long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		 return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}

}