package com.junyou.bus.guild.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.guild.entity.RoleGuildDuihuan;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleGuildDuihuanDao extends BusAbsCacheDao<RoleGuildDuihuan> implements IDaoOperation<RoleGuildDuihuan> {

	public List<RoleGuildDuihuan> initRoleGuildDuihuan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}