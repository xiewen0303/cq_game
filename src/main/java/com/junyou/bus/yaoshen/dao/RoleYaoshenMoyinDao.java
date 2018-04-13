package com.junyou.bus.yaoshen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMoyin;
import com.junyou.bus.yaoshen.vo.YaoShenMoyinRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleYaoshenMoyinDao extends BusAbsCacheDao<RoleYaoshenMoyin> implements IDaoOperation<RoleYaoshenMoyin> {

	public List<RoleYaoshenMoyin> initRoleYaoshenMoyin(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	@SuppressWarnings("unchecked")
	public List<YaoShenMoyinRankVo> getMoyinRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectYaoShenMoyinRankToYaoShenMoyinRankVo", queryParams);
	
	}
}