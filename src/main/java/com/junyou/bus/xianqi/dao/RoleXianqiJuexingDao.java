package com.junyou.bus.xianqi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xianqi.entity.RoleXianqiJuexing;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleXianqiJuexingDao extends BusAbsCacheDao<RoleXianqiJuexing> implements IDaoOperation<RoleXianqiJuexing> {

	public List<RoleXianqiJuexing> initRoleXianqiJuexing(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}