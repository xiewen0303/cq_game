package com.junyou.bus.shenmo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.shenmo.entity.RoleKuafuArena4v4;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleKuafuArena4v4Dao extends BusAbsCacheDao<RoleKuafuArena4v4>
		implements IDaoOperation<RoleKuafuArena4v4> {

	public List<RoleKuafuArena4v4> initRoleKuafuArena4v4(Long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);

		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}

	public void cleanAllJifen(int initJifen) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("jifen", initJifen);
		update("cleanAllJifen4v4", queryParams);
	}
}