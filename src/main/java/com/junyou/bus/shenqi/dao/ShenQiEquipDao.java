package com.junyou.bus.shenqi.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.shenqi.entity.ShenQiEquip;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;

@Repository
public class ShenQiEquipDao extends BusAbsCacheDao<ShenQiEquip> implements IDaoOperation<ShenQiEquip>  {
	
	public List<ShenQiEquip> initShenQiEquip(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}
