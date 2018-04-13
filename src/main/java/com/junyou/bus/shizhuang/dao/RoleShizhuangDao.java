package com.junyou.bus.shizhuang.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.shizhuang.entity.RoleShizhuang;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleShizhuangDao extends BusAbsCacheDao<RoleShizhuang> implements IDaoOperation<RoleShizhuang> {

	public List<RoleShizhuang> initRoleShizhuang(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}