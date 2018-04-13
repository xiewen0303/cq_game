package com.junyou.bus.huajuan2.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2;


@Repository
public class RoleHuajuan2Dao extends BusAbsCacheDao<RoleHuajuan2> implements IDaoOperation<RoleHuajuan2> {

	public List<RoleHuajuan2> initRoleHuajuan2(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}