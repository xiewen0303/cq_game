package com.junyou.bus.wenquan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.wenquan.entity.RoleWenquan;
import com.junyou.bus.wenquan.vo.WenquanRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleWenquanDao extends BusAbsCacheDao<RoleWenquan> implements IDaoOperation<RoleWenquan> {

	public List<RoleWenquan> initRoleWenquan(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}

	public List<WenquanRankVo> getWenquanRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectWenquanRankVo", queryParams);
	
	}
}