package com.junyou.bus.platform.yuenan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.yuenan.entity.YuenanYaoqing;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class YuenanYaoqingDao extends BusAbsCacheDao<YuenanYaoqing> implements IDaoOperation<YuenanYaoqing> {

	public List<YuenanYaoqing> initYuenanYaoqing(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}