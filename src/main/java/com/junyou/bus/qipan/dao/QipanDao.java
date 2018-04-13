package com.junyou.bus.qipan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.qipan.entity.Qipan;
import com.junyou.bus.share.dao.BusAbsCacheDao;


@Repository
public class QipanDao extends BusAbsCacheDao<Qipan> implements IDaoOperation<Qipan> {

	public List<Qipan> initQipan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}