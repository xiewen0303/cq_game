package com.junyou.bus.xingkongbaozang.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xingkongbaozang.entity.RefabuXkbz;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuXkbzDao extends BusAbsCacheDao<RefabuXkbz> implements IDaoOperation<RefabuXkbz> {

	public List<RefabuXkbz> initRefabuXkbz(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	

}