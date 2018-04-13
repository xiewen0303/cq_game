package com.junyou.bus.xinwen.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xinwen.entity.RoleTangbaoXinwen;
import com.junyou.bus.xinwen.vo.XinwenRankVo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RoleTangbaoXinwenDao extends BusAbsCacheDao<RoleTangbaoXinwen> implements IDaoOperation<RoleTangbaoXinwen> {

	public List<RoleTangbaoXinwen> initRoleTangbaoXinwen(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	@SuppressWarnings("unchecked")
	public List<XinwenRankVo> getXinwenRankVo(int limit) {
		QueryParamMap<String, Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("limit", limit);
		return query("selectXinwenRankToXinwenRankVo", queryParams);
	}
}