package com.junyou.bus.xiulianzhilu.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xiulianzhilu.entity.RoleXiulianTask;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleXiulianTaskDao extends BusAbsCacheDao<RoleXiulianTask> implements IDaoOperation<RoleXiulianTask> {

	public List<RoleXiulianTask> initRoleXiulianTask(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}