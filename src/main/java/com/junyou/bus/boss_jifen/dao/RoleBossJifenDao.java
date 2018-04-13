package com.junyou.bus.boss_jifen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.boss_jifen.entity.RoleBossJifen;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleBossJifenDao extends BusAbsCacheDao<RoleBossJifen> implements IDaoOperation<RoleBossJifen> {
	
	public List<RoleBossJifen> initRoleBossJifen(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}
