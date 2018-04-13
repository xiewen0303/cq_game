package com.junyou.bus.xunbao.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.xunbao.entity.RefbXunbao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefbXunBaoDao extends BusAbsCacheDao<RefbXunbao> implements IDaoOperation<RefbXunbao> {

	public List<RefbXunbao> initRefbXunbao(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		
		queryParams.put("userRoleId", userRoleId);
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
	public List<Integer> getAllXunbaoCount(Integer subId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("subId", subId);
		
		return query("selectAllXunbaoCountBySubId", queryParams);
	}
}