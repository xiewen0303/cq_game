package com.junyou.bus.jingji.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.jingji.entity.RoleJingji;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleJingjiDao extends AbsDao<RoleJingji> implements IDaoOperation<RoleJingji> {

	public List<RoleJingji> initAllRoleJingji() {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	
	public List<RoleJingji> initRoleJingji(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}