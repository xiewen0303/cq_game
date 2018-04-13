package com.junyou.bus.hczbs.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.junyou.bus.hczbs.entity.Zhengbasai;
import com.junyou.public_.share.dao.PublicAbsCacheDao;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class ZhengbasaiDao extends PublicAbsCacheDao<Zhengbasai> implements IDaoOperation<Zhengbasai> {

	public List<Zhengbasai> initZhengbasai() {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		
		return getRecords(queryParams, null, AccessType.getDirectDbType());
	}
}