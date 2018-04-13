package com.junyou.bus.platform.qq.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.platform.qq.entity.RoleQdianZhigou;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleQdianZhigouDao extends BusAbsCacheDao<RoleQdianZhigou> implements IDaoOperation<RoleQdianZhigou> {

	public List<RoleQdianZhigou> initRoleQdianZhigou(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}