package com.junyou.bus.jingji.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.jingji.entity.RoleJingjiAttribute;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleJingjiAttributeDao extends AbsDao<RoleJingjiAttribute> implements IDaoOperation<RoleJingjiAttribute> {

	public List<RoleJingjiAttribute> initRoleJingjiAttribute(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}