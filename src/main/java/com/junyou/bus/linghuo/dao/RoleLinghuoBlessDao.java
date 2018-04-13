package com.junyou.bus.linghuo.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.kernel.data.dao.IDaoOperation;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.QueryParamMap;
import com.junyou.bus.share.dao.BusAbsCacheDao;
import com.junyou.bus.linghuo.entity.RoleLinghuoBless;


@Repository
public class RoleLinghuoBlessDao extends BusAbsCacheDao<RoleLinghuoBless> implements IDaoOperation<RoleLinghuoBless> {

	public List<RoleLinghuoBless> initRoleLinghuoBless(Long userRoleId) {
		QueryParamMap<String,Object> queryParams = new QueryParamMap<String, Object>();
		queryParams.put("userRoleId", userRoleId);
		
		return getRecords(queryParams, userRoleId, AccessType.getDirectDbType());
	}
}