package com.junyou.bus.kaifuactivity.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.kaifuactivity.entity.ZhanliBipin;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Component
public class ZhanliBipinDao extends BusAbsCacheDao<ZhanliBipin> implements IDaoOperation<ZhanliBipin> {

	public List<ZhanliBipin> initZhanliBipin(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}