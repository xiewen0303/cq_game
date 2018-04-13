package com.junyou.bus.happycard.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.happycard.entity.RefabuHappyCardItem;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefabuHappyCardItemDao extends BusAbsCacheDao<RefabuHappyCardItem> implements IDaoOperation<RefabuHappyCardItem> {

	public List<RefabuHappyCardItem> initRefabuHappyCardItem(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}