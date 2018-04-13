package com.junyou.bus.platform.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.common.entity.Role360Vplan;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class Role360VplanDao extends BusAbsCacheDao<Role360Vplan> implements IDaoOperation<Role360Vplan> {

	public List<Role360Vplan> initRole360Vplan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}