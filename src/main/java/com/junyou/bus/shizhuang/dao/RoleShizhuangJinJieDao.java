package com.junyou.bus.shizhuang.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.shizhuang.entity.RoleShiZhuangJinJie;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleShizhuangJinJieDao extends BusAbsCacheDao<RoleShiZhuangJinJie> implements IDaoOperation<RoleShiZhuangJinJie> {

	public List<RoleShiZhuangJinJie> initRoleShizhuang(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}