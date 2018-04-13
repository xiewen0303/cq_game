package com.junyou.bus.platform.qq.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.platform.qq.entity.RoleQqGuanjia;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleQqGuanjiaDao extends BusAbsCacheDao<RoleQqGuanjia> implements IDaoOperation<RoleQqGuanjia> {

	public List<RoleQqGuanjia> initRoleQqGuanjia(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}