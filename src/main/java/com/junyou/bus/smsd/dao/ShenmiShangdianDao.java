package com.junyou.bus.smsd.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.smsd.entity.ShenmiShangdian;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class ShenmiShangdianDao extends BusAbsCacheDao<ShenmiShangdian> implements IDaoOperation<ShenmiShangdian> {

	public List<ShenmiShangdian> initShenmiShangdian(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}