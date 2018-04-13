package com.junyou.bus.platform._37.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.common.entity.Role37Vplan;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class Role37VplanDao extends BusAbsCacheDao<Role37Vplan> implements IDaoOperation<Role37Vplan> {

	public List<Role37Vplan> initRole37Vplan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}
