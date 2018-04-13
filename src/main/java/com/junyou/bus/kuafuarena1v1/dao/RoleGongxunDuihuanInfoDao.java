package com.junyou.bus.kuafuarena1v1.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.kuafuarena1v1.entity.RoleGongxunDuihuanInfo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleGongxunDuihuanInfoDao extends BusAbsCacheDao<RoleGongxunDuihuanInfo> implements IDaoOperation<RoleGongxunDuihuanInfo> {

	public List<RoleGongxunDuihuanInfo> initRoleGongxunDuihuanInfo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
}