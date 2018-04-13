package com.junyou.bus.fuben.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.fuben.entity.Fuben;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class FubenDao extends BusAbsCacheDao<Fuben> implements IDaoOperation<Fuben> {

	public List<Fuben> initFuben(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}