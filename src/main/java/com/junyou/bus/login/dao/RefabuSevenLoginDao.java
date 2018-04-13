package com.junyou.bus.login.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.login.entity.RefabuSevenLogin;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuSevenLoginDao extends BusAbsCacheDao<RefabuSevenLogin> implements IDaoOperation<RefabuSevenLogin> {

	public List<RefabuSevenLogin> initRefabuSevenLogin(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}