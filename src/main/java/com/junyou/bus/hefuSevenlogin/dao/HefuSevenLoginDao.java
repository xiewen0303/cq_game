package com.junyou.bus.hefuSevenlogin.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.hefuSevenlogin.entity.HefuSevenLogin;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class HefuSevenLoginDao extends BusAbsCacheDao<HefuSevenLogin> implements IDaoOperation<HefuSevenLogin> {

	public List<HefuSevenLogin> initHefuSevenLogin(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}