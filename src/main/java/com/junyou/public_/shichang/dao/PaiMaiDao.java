package com.junyou.public_.shichang.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.public_.shichang.entity.PaiMaiInfo;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.AbsDao;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class PaiMaiDao extends AbsDao<PaiMaiInfo> {

	public List<PaiMaiInfo> initPaiMai(long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}