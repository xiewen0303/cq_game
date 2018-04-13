package com.junyou.bus.tanbao.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.tanbao.entity.RefabuTanbao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuTanbaoDao extends BusAbsCacheDao<RefabuTanbao> implements IDaoOperation<RefabuTanbao> {

	public List<RefabuTanbao> initRefabuTanbao(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}