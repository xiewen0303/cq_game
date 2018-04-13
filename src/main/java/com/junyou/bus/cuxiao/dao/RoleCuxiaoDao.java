package com.junyou.bus.cuxiao.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.cuxiao.entity.RoleCuxiao;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleCuxiaoDao extends BusAbsCacheDao<RoleCuxiao> implements IDaoOperation<RoleCuxiao> {

	public List<RoleCuxiao> initRoleCuxiao(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}