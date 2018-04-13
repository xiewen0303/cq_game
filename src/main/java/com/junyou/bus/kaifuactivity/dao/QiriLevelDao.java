package com.junyou.bus.kaifuactivity.dao;


import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.kaifuactivity.entity.QiriLevel;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Component
public class QiriLevelDao  extends BusAbsCacheDao<QiriLevel> implements IDaoOperation<QiriLevel>  {

	
	public List<QiriLevel> dbLoadAllById(Integer id,Integer subId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("configId", id);
		queryParams.put("subId", subId);
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	public List<QiriLevel> dbLoadAll(int subId) {
		
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("subId", subId);
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
	
	public void dbInsert(QiriLevel qiriLevel) {
		insert(qiriLevel, null, AccessType.getDirectDbType());
	}
	public void dbUpdate(QiriLevel qiriLevel) {
		update(qiriLevel, null, AccessType.getDirectDbType());
	}
}