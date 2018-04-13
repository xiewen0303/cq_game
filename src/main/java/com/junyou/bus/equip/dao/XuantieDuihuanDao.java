package com.junyou.bus.equip.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.equip.entity.XuantieDuihuan;
import com.junyou.bus.share.dao.BusAbsCacheDao;


@Repository
public class XuantieDuihuanDao extends BusAbsCacheDao<XuantieDuihuan> {

	public List<XuantieDuihuan> initXuantieDuihuan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}