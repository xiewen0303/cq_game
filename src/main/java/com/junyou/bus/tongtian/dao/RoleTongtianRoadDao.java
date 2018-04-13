package com.junyou.bus.tongtian.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.tongtian.entity.RoleTongtianRoad;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleTongtianRoadDao extends BusAbsCacheDao<RoleTongtianRoad> implements IDaoOperation<RoleTongtianRoad> {

	public List<RoleTongtianRoad> initRoleTongtianRoad(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}