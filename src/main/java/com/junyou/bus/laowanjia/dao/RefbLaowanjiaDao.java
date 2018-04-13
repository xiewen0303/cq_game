package com.junyou.bus.laowanjia.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.laowanjia.entity.RefbLaowanjia;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefbLaowanjiaDao extends BusAbsCacheDao<RefbLaowanjia> implements IDaoOperation<RefbLaowanjia> {

	public List<RefbLaowanjia> initRefbLaowanjia(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}