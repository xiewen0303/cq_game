package com.junyou.bus.rfbactivity.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleYuanbaoRecordDao extends BusAbsCacheDao<RoleYuanbaoRecord> implements IDaoOperation<RoleYuanbaoRecord> {

	public List<RoleYuanbaoRecord> initRoleYuanbaoRecord(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}