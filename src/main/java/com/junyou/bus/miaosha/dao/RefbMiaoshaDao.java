package com.junyou.bus.miaosha.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.kernel.data.dao.IDaoOperation;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.miaosha.entity.RefbMiaosha;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;


@Repository
public class RefbMiaoshaDao extends BusAbsCacheDao<RefbMiaosha> implements IDaoOperation<RefbMiaosha> {

	public List<RefbMiaosha> initRefbMiaosha(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}