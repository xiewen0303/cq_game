package com.junyou.bus.kaifuactivity.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.kaifuactivity.entity.KaifuActity;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Component
public class KaifuActityDao extends BusAbsCacheDao<KaifuActity> implements IDaoOperation<KaifuActity> {

	public List<KaifuActity> dbLoadAll(int subId) {
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("subId", subId);
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	
	public void dbInsert(KaifuActity qiriLevel) {
		insert(qiriLevel, null, AccessType.getDirectDbType());
	}

}