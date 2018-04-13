package com.junyou.bus.fuben.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.fuben.entity.WuxingShilianFuben;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class WuxingShilianFubenDao extends BusAbsCacheDao<WuxingShilianFuben> implements IDaoOperation<WuxingShilianFuben> {

	public List<WuxingShilianFuben> initWuxingShilianFuben(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}