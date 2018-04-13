package com.junyou.bus.kaifuactivity.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.kaifuactivity.entity.QiriLevelLibao;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Component
public class QiriLevelLibaoDao extends BusAbsCacheDao<QiriLevelLibao> implements IDaoOperation<QiriLevelLibao>   {

	public List<QiriLevelLibao> initAll(long userRoleId){
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}