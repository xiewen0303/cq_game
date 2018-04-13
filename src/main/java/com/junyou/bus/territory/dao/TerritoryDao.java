package com.junyou.bus.territory.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.territory.entity.Territory;
import com.junyou.public_.share.dao.PublicAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class TerritoryDao extends PublicAbsCacheDao<Territory> implements IDaoOperation<Territory> {

	public List<Territory> initTerritory() {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
}