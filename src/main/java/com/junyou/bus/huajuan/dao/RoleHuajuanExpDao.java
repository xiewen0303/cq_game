package com.junyou.bus.huajuan.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.huajuan.entity.RoleHuajuanExp;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleHuajuanExpDao extends BusAbsCacheDao<RoleHuajuanExp> implements IDaoOperation<RoleHuajuanExp> {

	public List<RoleHuajuanExp> initRoleHuajuanExp(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}