package com.junyou.bus.lianchong.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.lianchong.entity.RoleLianchong;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleLianchongDao extends BusAbsCacheDao<RoleLianchong> implements IDaoOperation<RoleLianchong> {

	public List<RoleLianchong> initRoleLianchong(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}