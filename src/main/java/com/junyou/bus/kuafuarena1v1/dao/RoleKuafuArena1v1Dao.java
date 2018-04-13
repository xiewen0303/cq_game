package com.junyou.bus.kuafuarena1v1.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.kuafuarena1v1.entity.RoleKuafuArena1v1;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class RoleKuafuArena1v1Dao extends BusAbsCacheDao<RoleKuafuArena1v1>
		implements IDaoOperation<RoleKuafuArena1v1> {

	public List<RoleKuafuArena1v1> initRoleKuafuArena1v1(Long userRoleId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);

		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}

	public void cleanAllJifen(int initJifen) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("jifen", initJifen);
		update("cleanAllJifen", queryParams);
	}
}