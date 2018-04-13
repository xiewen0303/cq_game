package com.junyou.bus.yaoshen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshenHunpo;
import com.junyou.bus.yaoshen.vo.YaoShenHunpoRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleYaoshenHunpoDao extends BusAbsCacheDao<RoleYaoshenHunpo> implements IDaoOperation<RoleYaoshenHunpo> {

	public List<RoleYaoshenHunpo> initRoleYaoshenHunpo(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	@SuppressWarnings("unchecked")
	public List<YaoShenHunpoRankVo> getYaoShenHunpoRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectYaoShenHunpoRankToYaoShenHunpoRankVo", queryParams);
	
	}
}