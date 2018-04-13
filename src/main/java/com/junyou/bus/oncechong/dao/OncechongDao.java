package com.junyou.bus.oncechong.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.oncechong.entity.RoleOncechong;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class OncechongDao extends BusAbsCacheDao<RoleOncechong> implements IDaoOperation<RoleOncechong> {

	public List<RoleOncechong> initOncechong(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}