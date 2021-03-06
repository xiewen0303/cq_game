package com.junyou.bus.rfbflower.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.rfbflower.entity.RoleRfbFlower;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleRfbFlowerDao extends BusAbsCacheDao<RoleRfbFlower> implements IDaoOperation<RoleRfbFlower> {

	public List<RoleRfbFlower> initRoleRfbFlower(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}