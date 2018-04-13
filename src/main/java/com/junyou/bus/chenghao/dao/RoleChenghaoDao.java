package com.junyou.bus.chenghao.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.chenghao.entity.RoleChenghao;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleChenghaoDao extends BusAbsCacheDao<RoleChenghao> implements IDaoOperation<RoleChenghao> {

	public List<RoleChenghao> initRoleChenghao(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleChenghao> getChenghao(Long userRoleId,Integer chenghaoId) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		queryParams.put("chenghaoId", chenghaoId);
		return query("selectSingleByParamsRoleChenghao", queryParams);
	
	}
}