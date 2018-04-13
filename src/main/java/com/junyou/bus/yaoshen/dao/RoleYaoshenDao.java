package com.junyou.bus.yaoshen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.vo.YaoShenRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleYaoshenDao extends BusAbsCacheDao<RoleYaoshen> implements IDaoOperation<RoleYaoshen> {

	public List<RoleYaoshen> initRoleYaoshen(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	
	@SuppressWarnings("unchecked")
	public List<YaoShenRankVo> getYaoShenRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectYaoShenRankToYaoShenRankVo", queryParams);
	
	}
}