package com.junyou.bus.jingji.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.jingji.entity.RoleJingjiDuihuan;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleJingjiDuihuanDao extends BusAbsCacheDao<RoleJingjiDuihuan> implements IDaoOperation<RoleJingjiDuihuan> {

	public List<RoleJingjiDuihuan> initRoleJingjiDuihuan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}