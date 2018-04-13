package com.junyou.bus.equip.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.equip.entity.RoleXiangwei;
import com.junyou.bus.share.dao.BusAbsCacheDao;

/**
 * 
 * @description:套装象位持久层 
 *
 *	@author ChuBin
 *
 * @date 2016-12-13
 */
@Repository
public class RoleXiangweiDao extends BusAbsCacheDao<RoleXiangwei> {

	public List<RoleXiangwei> initRoleXiangwei(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}