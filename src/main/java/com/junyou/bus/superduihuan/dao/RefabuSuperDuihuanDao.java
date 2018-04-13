package com.junyou.bus.superduihuan.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.superduihuan.entity.RefabuSuperDuihuan;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuSuperDuihuanDao extends BusAbsCacheDao<RefabuSuperDuihuan> implements IDaoOperation<RefabuSuperDuihuan> {

	public List<RefabuSuperDuihuan> initRefabuSuperDuihuan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}