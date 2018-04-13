package com.junyou.bus.zhuanpan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.zhuanpan.entity.RefabuZhuanpan;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuZhuanpanDao extends BusAbsCacheDao<RefabuZhuanpan> implements IDaoOperation<RefabuZhuanpan> {

	public List<RefabuZhuanpan> initRefabuZhuanpan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}