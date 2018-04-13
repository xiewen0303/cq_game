package com.junyou.bus.jewel.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.jewel.entity.RoleJewel;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleJewelDao extends BusAbsCacheDao<RoleJewel> implements IDaoOperation<RoleJewel> {

	public List<RoleJewel> initRoleJewel(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}