package com.junyou.bus.huajuan2.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.huajuan2.entity.RoleHuajuan2Exp;


@Repository
public class RoleHuajuan2ExpDao extends BusAbsCacheDao<RoleHuajuan2Exp> implements IDaoOperation<RoleHuajuan2Exp> {

	public List<RoleHuajuan2Exp> initRoleHuajuan2Exp(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}