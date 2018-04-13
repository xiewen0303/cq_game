package com.junyou.bus.flower.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.flower.entity.RoleSendFlower;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleSendFlowerDao extends BusAbsCacheDao<RoleSendFlower> implements IDaoOperation<RoleSendFlower> {

	public List<RoleSendFlower> initRoleSendFlower(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}