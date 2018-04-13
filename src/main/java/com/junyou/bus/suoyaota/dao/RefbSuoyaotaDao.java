package com.junyou.bus.suoyaota.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.suoyaota.entity.RefbSuoyaota;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefbSuoyaotaDao extends BusAbsCacheDao<RefbSuoyaota> implements IDaoOperation<RefbSuoyaota> {

	public List<RefbSuoyaota> initRefbSuoyaota(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}