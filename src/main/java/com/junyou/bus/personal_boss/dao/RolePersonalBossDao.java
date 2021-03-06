package com.junyou.bus.personal_boss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.personal_boss.entity.RolePersonalBoss;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RolePersonalBossDao extends BusAbsCacheDao<RolePersonalBoss> implements IDaoOperation<RolePersonalBoss> {

	public List<RolePersonalBoss> initRolePersonalBoss(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}